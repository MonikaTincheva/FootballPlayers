package com.example.footballsystem.services;

import com.example.footballsystem.helpers.DateHelper;
import com.example.footballsystem.helpers.EntityHelper;
import com.example.footballsystem.models.entity.Match;
import com.example.footballsystem.models.entity.Team;
import com.example.footballsystem.repositories.MatchRepository;
import com.example.footballsystem.repositories.RecordRepository;
import com.example.footballsystem.repositories.TeamRepository;
import com.example.footballsystem.services.interfaces.MatchService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository repository;
    private final TeamRepository teamRepository;


    public MatchServiceImpl(MatchRepository repository, TeamRepository teamRepository, RecordRepository recordRepository) {
        this.repository = repository;
        this.teamRepository = teamRepository;

    }

    @Override
    public List<Match> getAll() {
        return repository.findAll();
    }

    @Override
    public String processCSVFile(MultipartFile file) {

        StringBuilder builder = new StringBuilder();
        List<Match> matches = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                try {

                    String[] data = line.split(",");
                    long idATeam = Long.parseLong(data[1]);
                    long idBTeam = Long.parseLong(data[2]);

                    LocalDate date = DateHelper.parseDate(data[3]);
                    String score = data[4];

                    Team ATeam = teamRepository.findById(idATeam)
                            .orElseThrow(() -> new NoSuchElementException("Team with ID " + idATeam + " not found"));

                    Team BTeam = teamRepository.findById(idBTeam)
                            .orElseThrow(() -> new NoSuchElementException("Team with ID " + idBTeam + " not found"));
                    if (EntityHelper.isValidScore(score)) {
                        Match match = repository.findByATeamsAndBTeamsAndDate(ATeam, BTeam, date);
                        if (match == null) {
                            matches.add(new Match(ATeam, BTeam, date, score));
                        } else {
                            builder.append("А Match with parameters: " + line + " already exists!").append(System.lineSeparator());
                        }

                    } else {
                        builder.append("Invalid parameters on one of the lines: " + line + "!").append(System.lineSeparator());
                    }


                } catch (IllegalArgumentException e) {
                    builder.append(String.format(
                            "Invalid parameters on one of the lines: " + line + "!")).append(System.lineSeparator());
                } catch (NoSuchElementException e) {
                    builder.append(e.getMessage()).append(System.lineSeparator());
                }
                repository.saveAll(matches);
            }
        } catch (IOException e) {
            return "Invalid file!";
        }


        if (matches.isEmpty()) {
            return builder.toString();
        }
        return builder.append(String.format("You have successfully added %d matches", matches.size())).toString();
    }

    @Override
    public String delete(long id) {

        Match match = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found"));

        repository.delete(match);
        return "You have successfully deleted a match whit id: " + id;
    }
}
