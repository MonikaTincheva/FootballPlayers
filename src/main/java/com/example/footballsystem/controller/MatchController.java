package com.example.footballsystem.controller;

import com.example.footballsystem.models.entity.Match;
import com.example.footballsystem.repositories.MatchRepository;
import com.example.footballsystem.repositories.RecordRepository;
import com.example.footballsystem.services.interfaces.MatchService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchService service;


    public MatchController(MatchService service, RecordRepository repository, MatchRepository repository1, RecordRepository recordRepository) {
        this.service = service;

    }


    @GetMapping
    public List<Match> get() {
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


    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        try {
            return service.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}



