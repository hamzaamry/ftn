#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import sys
import os
import re
import json
import argparse
import requests
from PyPDF2 import PdfReader
from pdf2image import convert_from_path
from PIL import Image, ImageFilter, ImageEnhance
import pytesseract

if hasattr(sys.stdout, "reconfigure"):
    sys.stdout.reconfigure(encoding="utf-8")
    sys.stderr.reconfigure(encoding="utf-8")

pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"

def get_pdf_links(base_url):
    resp = requests.get(base_url); resp.raise_for_status()
    raw = re.findall(r'href=[\'"]([^\'"]+?\.(?:pdf|jpe?g))(?:[\'"]|$)', resp.text, flags=re.IGNORECASE)
    links = {
        href if href.lower().startswith(('http://','https://'))
        else requests.compat.urljoin(base_url, href)
        for href in raw
    }
    return sorted(l for l in links if l.startswith('http://ftnatation.tn/wp-content'))

def preprocess_image(img):
    gray = img.convert('L')
    enhanced = ImageEnhance.Contrast(gray).enhance(2.0)
    return enhanced.filter(ImageFilter.MedianFilter(size=3))

def download_and_extract(url, download_dir):
    ext = os.path.splitext(url)[1].lower()
    fname = os.path.basename(url)
    local_path = os.path.join(download_dir, fname)
    if not os.path.exists(local_path):
        print(f"Downloading → {url}")
        r = requests.get(url); r.raise_for_status()
        with open(local_path, 'wb') as f: f.write(r.content)

    text = ""
    if ext in {'.jpg', '.jpeg', '.png'}:
        img = Image.open(local_path)
        proc = preprocess_image(img)
        text = pytesseract.image_to_string(proc, lang='fra+eng')
        pdf_bytes = pytesseract.image_to_pdf_or_hocr(proc, extension='pdf')
        pdf_name = os.path.splitext(fname)[0] + '.pdf'
        pdf_path = os.path.join(download_dir, pdf_name)
        with open(pdf_path, 'wb') as f: f.write(pdf_bytes)
        fname = pdf_name
    else:
        reader = PdfReader(local_path)
        pages = [p.extract_text() or "" for p in reader.pages]
        text = "\n".join(pages).strip()
        if len(text) < 200:
            ocr = []
            for img in convert_from_path(local_path, dpi=300):
                ocr.append(pytesseract.image_to_string(preprocess_image(img), lang='fra+eng'))
            text = "\n".join(ocr)
    return fname, text

def parse_txt(txt):
    t = re.sub(r'(\d)([A-Z]{3})', r'\1 \2', txt)
    t = re.sub(r'\)(\d)', r') \1', t)
    t = re.sub(r'(\d)\.(?=[A-Z])', r'\1. ', t)
    t = re.sub(r'\n{2,}', '\n', t).strip()
    meta = {'location': None, 'date': None, 'temperature_air': None, 'temperature_water': None}

    m = re.search(r'([A-ZÉÈÀÇ\' ]+?)\s*-\s*(\d{2}/\d{2}/\d{4})', t)
    if m:
        meta['location'], meta['date'] = m.group(1).title(), m.group(2)

    m2 = re.search(r'Températures\s*\(Air/Eau\):\s*([\d.]+)(?:\s*/\s*([\d.]+))?', t)
    if m2:
        meta['temperature_air'], meta['temperature_water'] = m2.group(1), m2.group(2)

    sections = re.split(r'\n(?=(?:Dames|Messieurs)\s*-)', t)
    events = []
    for sec in sections:
        h = re.match(r'(Dames|Messieurs)\s*-\s*([A-ZÉÈ ]+)', sec)
        if not h: continue
        gender, category = h.group(1), h.group(2).title()
        lines = [L.strip() for L in sec.splitlines() if re.search(r'\d{1,2}:\d{2}(?::\d{2}(?:\.\d+)?)?', L)]
        results = []
        for idx, line in enumerate(lines, 1):
            tm = re.search(r'\d{1,2}:\d{2}(?::\d{2}(?:\.\d+)?)?', line)
            time_str = tm.group(0)
            left, right = line[:tm.start()].strip(), line[tm.end():].strip()
            name = re.sub(r'^\d+\.\s*', '', left)
            parts = right.split()
            nat = parts[0] if parts else None
            birth = parts[1] if len(parts) > 1 and parts[1].isdigit() else None
            bib_m = re.search(r'\((\d+)\)', right)
            bib = int(bib_m.group(1)) if bib_m else None
            pts = next((int(tok[:-1]) for tok in parts if tok.endswith('.') and tok[:-1].isdigit()), None)
            club = None
            if pts is not None and f"{pts}." in parts:
                club = ' '.join(parts[parts.index(f"{pts}.")+1:])
            results.append({
                'rank': idx,
                'name': name,
                'bib': bib,
                'nationality': nat,
                'birth_year': int(birth) if birth else None,
                'club': club,
                'time': time_str,
                'points': pts
            })
        events.append({'gender': gender, 'category': category, 'results': results})
    return meta, events

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--base-url', required=True)
    parser.add_argument('--download-dir', default='./pdfs')
    parser.add_argument('--json-dir', default='./json')
    args = parser.parse_args()

    os.makedirs(args.download_dir, exist_ok=True)
    os.makedirs(args.json_dir, exist_ok=True)

    links = get_pdf_links(args.base_url)
    if not links:
        print("No files found—check your base URL or network connection.")
        return

    print(f"Found {len(links)} files.")
    for url in links:
        fname, txt = download_and_extract(url, args.download_dir)
        meta, events = parse_txt(txt)
        out = {'source_url': url, 'filename': fname, 'metadata': meta, 'events': events}
        jpath = os.path.join(args.json_dir, fname.replace('.pdf', '.json'))
        with open(jpath, 'w', encoding='utf-8') as jf:
            json.dump(out, jf, ensure_ascii=False, indent=2)
        print(f"Saved → {jpath}")

if __name__ == '__main__':
    main()
