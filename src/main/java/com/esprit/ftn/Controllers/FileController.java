package com.esprit.ftn.Controllers;

import com.esprit.ftn.entities.FileDetail;
import com.esprit.ftn.services.ImportService;
import com.esprit.ftn.services.ScriptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {
    private final ScriptService scriptSvc;
    private final ImportService importSvc;

    public FileController(ScriptService scriptSvc, ImportService importSvc) {
        this.scriptSvc = scriptSvc;
        this.importSvc = importSvc;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importData() {
        try {
            scriptSvc.runScript();
            importSvc.importJsonFiles();
            return ResponseEntity.ok("Import completed successfully.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Import failed: " + e.getMessage());
        }
    }

    @GetMapping("/files")
    public List<FileDetail> listFiles() {
        return importSvc.getAllFiles();
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<FileDetail> getOne(@PathVariable Long id) {
        return importSvc.getFileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/files/{id}")
    public ResponseEntity<FileDetail> updateFile(@PathVariable Long id, @RequestBody FileDetail updated) {
        return importSvc.getFileById(id)
                .map(existing -> {
                    // Update fields if new value is not null
                    if (updated.getSourceUrl() != null) existing.setSourceUrl(updated.getSourceUrl());
                    if (updated.getFilename() != null) existing.setFilename(updated.getFilename());
                    if (updated.getLocation() != null) existing.setLocation(updated.getLocation());
                    if (updated.getDate() != null) existing.setDate(updated.getDate());
                    if (updated.getTemperatureAir() != null) existing.setTemperatureAir(updated.getTemperatureAir());
                    if (updated.getTemperatureWater() != null) existing.setTemperatureWater(updated.getTemperatureWater());
                    if (updated.getEvents() != null) existing.setEvents(updated.getEvents());

                    importSvc.saveFile(existing);
                    return ResponseEntity.ok(existing);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        if (importSvc.getFileById(id).isPresent()) {
            importSvc.deleteFile(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
