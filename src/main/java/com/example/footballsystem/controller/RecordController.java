package com.example.footballsystem.controller;

import com.example.footballsystem.models.entity.Record;
import com.example.footballsystem.services.interfaces.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {
    private final RecordService service;

    @Autowired
    public RecordController(RecordService service) {
        this.service = service;
    }


    @GetMapping
    public List<Record> get() {
        return service.getAll();

    }
    @PostMapping("/file")
    public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            return ResponseEntity.ok(service.processCSVFile(multipartFile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
