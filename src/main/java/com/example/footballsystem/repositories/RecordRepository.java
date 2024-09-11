package com.example.footballsystem.repositories;

import com.example.footballsystem.models.entity.Match;
import com.example.footballsystem.models.entity.Player;
import com.example.footballsystem.models.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

 Record getRecordByPlayerAndAndMatchAndFromMinutesAndToMinutes(Player player, Match match, int fromMinutes, int toMinutes);

 List<Record> findByPlayer(Player player);
}

