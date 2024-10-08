package com.example.footballsystem.controller;

import com.example.footballsystem.models.entity.Team;
import com.example.footballsystem.services.interfaces.TeamService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService service;

    @Autowired
    public TeamController(TeamService teamService) {
        this.service = teamService;
    }

    @GetMapping
    public List<Team> get() {
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
