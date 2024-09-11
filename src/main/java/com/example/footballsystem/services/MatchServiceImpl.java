package com.example.footballsystem.services;

import com.example.footballsystem.exceptions.EntityDuplicateException;
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
import java.io.InputStreamReader;
import java.time.LocalDate;
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
        int countMatch = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    if (data.length < 5) {
                        throw new IndexOutOfBoundsException();
                    }
                    Match match = createMatch(data);
                    repository.save(match);
                    countMatch++;

                } catch (NumberFormatException e) {
                    builder.append("Invalid parameters are Match with id:" + e.getMessage() + "!").append(System.lineSeparator());
                } catch (IndexOutOfBoundsException e) {
                    builder.append("Invalid count parameters of line: " + line + "!").append(System.lineSeparator());
                } catch (Exception e) {
                    builder.append(e.getMessage()).append(System.lineSeparator());

                }
            }
        }catch (Exception e){
            return "Invalid file!";
        }
        if (countMatch==0) {
            return builder.toString();
        }
        return builder.append(String.format("You have successfully added %d matches", countMatch)).toString();

    }

    private Match createMatch(String[] data) {



        long idATeam = Long.parseLong(data[1]);
        long idBTeam = Long.parseLong(data[2]);

        LocalDate date = DateHelper.parseDate(data[3]);
        String score = data[4];

        Team ATeam = teamRepository.findById(idATeam)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + idATeam + " not found"));

        Team BTeam = teamRepository.findById(idBTeam)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + idBTeam + " not found"));

        if (repository.findByATeamsAndBTeamsAndDate(ATeam, BTeam, date) != null) {
            throw new EntityDuplicateException("Match", "ATeam and BTeam", ATeam.getName() +" "+BTeam.getName());
        }

        if (!EntityHelper.isValidScore(score)) {
            throw new NumberFormatException(data[1]);
        }

        return new Match(ATeam, BTeam, date, score);


    }

    @Override
    public String delete(long id) {

        Match match = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found"));

        repository.delete(match);
        return "You have successfully deleted a match whit id: " + id;
    }
}
