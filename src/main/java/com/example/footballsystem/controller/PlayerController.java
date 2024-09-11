package com.example.footballsystem.controller;

import com.example.footballsystem.models.entity.Player;
import com.example.footballsystem.services.interfaces.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService service;

    @Autowired
    public PlayerController(PlayerService service) {
        this.service = service;
    }


    @GetMapping
    public List<Player> get() {
        return service.getAll();

    }

    @GetMapping("/pair")
    public String getPairOfPlayers() {
        return service.getPairOfFootballPlayers();

    }

    @PostMapping("/file")
    public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile multipartFile) {

        try {
            return ResponseEntity.ok(service.processCSVFile(multipartFile));
        }
        catch (Exception e) {
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
