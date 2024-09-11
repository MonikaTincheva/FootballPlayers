package com.example.footballsystem.repositories;

import com.example.footballsystem.models.entity.Match;
import com.example.footballsystem.models.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    Match findByATeamsAndBTeamsAndDate(Team ATeam, Team BTeam, LocalDate date);

}
