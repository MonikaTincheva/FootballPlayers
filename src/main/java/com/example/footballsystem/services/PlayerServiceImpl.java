package com.example.footballsystem.services;

import com.example.footballsystem.exceptions.EntityDuplicateException;
import com.example.footballsystem.helpers.EntityHelper;
import com.example.footballsystem.models.*;
import com.example.footballsystem.models.entity.Record;
import com.example.footballsystem.models.entity.Player;
import com.example.footballsystem.models.entity.Team;
import com.example.footballsystem.repositories.MatchRepository;
import com.example.footballsystem.repositories.PlayerRepository;
import com.example.footballsystem.repositories.RecordRepository;
import com.example.footballsystem.repositories.TeamRepository;
import com.example.footballsystem.services.interfaces.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository repository;


    private final TeamRepository teamRepository;

    private final RecordRepository recordRepository;


    public PlayerServiceImpl(PlayerRepository repository, TeamRepository teamRepository, RecordRepository recordRepository, MatchRepository matchRepository) {
        this.repository = repository;
        this.teamRepository = teamRepository;
        this.recordRepository = recordRepository;

    }

    @Override
    public List<Player> getAll() {
        return repository.findAll();
    }

    @Override
    public String processCSVFile(MultipartFile file) {

        StringBuilder stringBuilder = new StringBuilder();
        int countPlayer = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {

                try {
                    String[] data = line.split(",");
                    if (data.length < 5) {
                        throw new IndexOutOfBoundsException();
                    }
                    Player player = createPlayer(data);
                    countPlayer++;
                    repository.save(player);

                } catch (NumberFormatException e) {
                    stringBuilder.append("Invalid parameters are Player with id:" + e.getMessage() + "!").append(System.lineSeparator());
                } catch (IndexOutOfBoundsException e) {
                    stringBuilder.append("Invalid count parameters of line: " + line + "!").append(System.lineSeparator());
                } catch (Exception e) {
                    stringBuilder.append(e.getMessage()).append(System.lineSeparator());

                }
            }

        } catch (IOException e) {
            return "Invalid file!";
        }

        if (countPlayer == 0) {
            return stringBuilder.toString();
        }
        return stringBuilder.append(String.format("You have successfully added %d players", countPlayer)).toString();
    }

    private Player createPlayer(String[] data) {

        int teamNumber = Integer.parseInt(data[1]);
        String position = data[2];
        String playerName = data[3];
        Long id = Long.parseLong(data[4]);

        if (!EntityHelper.validateName(playerName) || !EntityHelper.validatePosition(position)
                || !EntityHelper.validateTeamNumber(teamNumber)) {
            throw new NumberFormatException(data[1]);
        }

        if (repository.findPlayerByFullNameAndAndPosition(playerName, position) != null) {
            throw new EntityDuplicateException("Player", "fullName", playerName);
        }

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Team with %d not found!", id)));

        Player player = new Player(playerName, teamNumber, position, team);

        return player;
    }


    @Override
    public String getPairOfFootballPlayers() {
        List<Record> records = recordRepository.findAll();
        Map<Pair, Integer> pairs = new HashMap<>();
        Map<Pair, PairRecord> pairRecordPlay = new HashMap<>();


        for (Record record : records) {
            for (Record record2 : records) {
                if (record.getMatch().equals(record2.getMatch()) && !record.getPlayer().equals(record2.getPlayer())
                        && record.getPlayer().getTeam().getId() != record2.getPlayer().getTeam().getId()) {
                    Player first = record.getPlayer();
                    Player second = record2.getPlayer();
                    Pair pair = new Pair(first, second);
                    int totalTimeWhitPlay = timeOfPlayedTogether(record.getFromMinutes(), record.getToMinutes(),
                            record2.getFromMinutes(), record2.getToMinutes());

                    if (totalTimeWhitPlay > 0) {
                        long matchId = record.getMatch().getId();
                        if (pairs.containsKey(pair)) {
                            pairs.put(pair, pairs.get(pair) + totalTimeWhitPlay);
                            PairRecord pairRecord = pairRecordPlay.get(pair);
                            Map<Long, Integer> matchesRecord = pairRecord.getMatchesRecord();
                            if (matchesRecord.containsKey(matchId)) {
                                matchesRecord.put(matchId, matchesRecord.get(matchId) + totalTimeWhitPlay);
                                pairRecord.setMatchesRecord(matchesRecord);
                            } else {
                                matchesRecord.put(matchId, totalTimeWhitPlay);
                            }

                            pairRecordPlay.put(pair, pairRecord);

                        } else {
                            pairs.put(pair, totalTimeWhitPlay);
                            PairRecord pairRecord = new PairRecord(matchId, totalTimeWhitPlay);
                            pairRecordPlay.put(pair, pairRecord);
                        }
                    }
                }
            }
        }

        Optional<Integer> maxValue = pairs.values().stream().max(Integer::compare);


        if (maxValue.isPresent()) {
            int max = maxValue.get();
            System.out.println(max);
            List<Map.Entry<Pair, Integer>> maxEntries = pairs.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(max))
                    .collect(Collectors.toList());

            if (!maxEntries.isEmpty()) {
                StringBuilder builder = new StringBuilder();

                for (Map.Entry<Pair, Integer> entry : maxEntries) {
                    Pair playerPair = entry.getKey();
                    PairRecord pairRecord = pairRecordPlay.get(playerPair);
                    builder.append(String.format("%d, %d, %d", playerPair.getPlayerOne().getId(), playerPair.getPlayerTwo().getId(), max)).append(System.lineSeparator());
                    pairRecord.getMatchesRecord().entrySet().forEach(e -> builder.append(String.format("%d, %d", e.getKey(), e.getValue())).append(System.lineSeparator()));
                }
                return builder.toString();

            }
        }
        return "No players found who played in a general match!";
    }

    private int timeOfPlayedTogether(int from1, int to1, int from2, int to2) {
        int start = Math.max(from1, from2);
        int end = Math.min(to1, to2);
        return Math.max(0, end - start);
    }

    @Override
    public String delete(long id) {
        Player player = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
        List<Record> records = recordRepository.findByPlayer(player);
        recordRepository.deleteAll(records);
        repository.delete(player);
        return "You have successfully deleted a played whit name: " + player.getFullName();
    }
}




