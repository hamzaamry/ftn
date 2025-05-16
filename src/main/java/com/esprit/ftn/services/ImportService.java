package com.esprit.ftn.services;

import com.esprit.ftn.Repositories.FileDetailRepository;
import com.esprit.ftn.entities.FileDetail;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ImportService {
    private final FileDetailRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public ImportService(FileDetailRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void importJsonFiles() throws IOException {
        File dir = new File("./scripts/json");
        if (!dir.exists()) return;

        for (File f : dir.listFiles((d, n) -> n.toLowerCase().endsWith(".json"))) {
            JsonNode root = mapper.readTree(f);
            String filename = root.path("filename").asText();

            JsonNode meta = root.path("metadata");
            FileDetail detail = new FileDetail();
            detail.setSourceUrl(root.path("source_url").asText());
            if (repo.existsByFilename(filename)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                filename= filename +"_" +LocalDateTime.now().format(formatter);
            }

            detail.setFilename(filename);
            detail.setLocation(meta.path("location").asText());
            detail.setDate(LocalDate.parse(meta.path("date").asText(), fmt));
            detail.setCreationdate(LocalDate.now());
            String air = meta.path("temperature_air").asText(null);
            detail.setTemperatureAir(
                    air != null && !air.isEmpty() ? Double.valueOf(air) : null
            );
            String water = meta.path("temperature_water").asText(null);
            detail.setTemperatureWater(
                    water != null && !water.isEmpty() ? Double.valueOf(water) : null
            );

            // store the raw events JSON array
            detail.setEvents(root.path("events").toString());

            repo.save(detail);
        }
    }

    public List<FileDetail> getAllFiles() {
        return repo.findAll();
    }

    public Optional<FileDetail> getFileById(Long id) {
        return repo.findById(id);
    }
    @Transactional
    public void saveFile(FileDetail file) {
        repo.save(file);
    }

    @Transactional
    public void deleteFile(Long id) {
        repo.deleteById(id);
    }

}
