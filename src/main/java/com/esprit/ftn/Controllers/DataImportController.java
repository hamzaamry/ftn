package com.esprit.ftn.Controllers;

import com.esprit.ftn.services.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/import")

public class DataImportController {

    @Autowired
    private DataImportService dataImportService;

    @PostMapping("/competitions")
    public ResponseEntity<String> importCompetitions(@RequestParam("file") MultipartFile file) {
        try {
            String jsonContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            dataImportService.importCompetitionsFromJson(jsonContent);
            return ResponseEntity.ok("Competitions imported successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error importing competitions: " + e.getMessage());
        }
    }

    @PostMapping("/events")
    public ResponseEntity<String> importEvents(@RequestParam("file") MultipartFile file) {
        try {
            String jsonContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            dataImportService.importEventsFromJson(jsonContent);
            return ResponseEntity.ok("Events imported successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error importing events: " + e.getMessage());
        }
    }

    @PostMapping("/clubs")
    public ResponseEntity<String> importClubs(@RequestParam("file") MultipartFile file) {
        try {
            String jsonContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            dataImportService.importClubsFromJson(jsonContent);
            return ResponseEntity.ok("Clubs imported successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error importing clubs: " + e.getMessage());
        }
    }

    @PostMapping("/rankings")
    public ResponseEntity<String> importRankings(@RequestParam("file") MultipartFile file) {
        try {
            String jsonContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            dataImportService.importRankingsFromJson(jsonContent);
            return ResponseEntity.ok("Rankings imported successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error importing rankings: " + e.getMessage());
        }
    }
}