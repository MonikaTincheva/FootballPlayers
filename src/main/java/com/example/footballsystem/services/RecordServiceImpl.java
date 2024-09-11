package com.example.footballsystem.services;

import com.example.footballsystem.helpers.EntityHelper;
import com.example.footballsystem.models.entity.Match;
import com.example.footballsystem.models.entity.Player;
import com.example.footballsystem.models.entity.Record;
import com.example.footballsystem.repositories.MatchRepository;
import com.example.footballsystem.repositories.PlayerRepository;
import com.example.footballsystem.repositories.RecordRepository;
import com.example.footballsystem.services.interfaces.RecordService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RecordServiceImpl implements RecordService {

    public final RecordRepository repository;
    public final PlayerRepository playerRepository;
    public final MatchRepository matchRepository;

    @Autowired
    public RecordServiceImpl(RecordRepository repository, PlayerRepository playerRepository, MatchRepository matchRepository) {
        this.repository = repository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Record> getAll() {
        return repository.findAll();
    }

    @Override
    public String processCSVFile(MultipartFile file) {
        StringBuilder builder = new StringBuilder();
        List<Record> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    long playerId = Long.parseLong(data[1].trim());
                    long matchId = Long.parseLong(data[2].trim());
                    int fromMinutes = Integer.parseInt(data[3].trim());
                    int toMinutes;

                    if(data[4].equalsIgnoreCase("null")){
                        toMinutes = 90;

                    }else {
                        toMinutes =Integer.parseInt(data[4]);
                    }

                     Player player = playerRepository.findById(playerId)
                             .orElseThrow(() -> new NoSuchElementException("Player with ID " + playerId + " not found"));


                   Match match = matchRepository.findById(matchId)
                           .orElseThrow(() -> new NoSuchElementException("Match with ID " + matchId + " not found"));
                   if(EntityHelper.validateMinutes(fromMinutes) && EntityHelper.validateMinutes(toMinutes)){
                       Record  record = repository
                               .getRecordByPlayerAndAndMatchAndFromMinutesAndToMinutes(player,match,fromMinutes,toMinutes);
                       if(record == null){
                           records.add(new Record(player,match,fromMinutes,toMinutes));
                       }
                       else {
                           builder.append("А Record with parameters: "+line+" already exists!").append(System.lineSeparator());
                       }

                   }
                   else{
                       builder.append(String.format(
                               "Invalid parameters on one of the lines: " + line +"!"));
                   }

                } catch (NumberFormatException | NoSuchElementException e) {
                    builder.append(e.getMessage()).append(System.lineSeparator());
                }
                repository.saveAll(records);
            }
        } catch (IOException e) {
            return "Invalid file!";
        }


        if (records.isEmpty()) {
            return builder.toString();
        }
        return builder.append(String.format("You have successfully added %d records", records.size())).toString();
    }

    @Override
    public String delete(long id) {
        Record record = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Record whit this %d does not exist!",id)));
        repository.delete(record);
        return "You have successfully deleted record whit id: "+record.getId();
    }


}

