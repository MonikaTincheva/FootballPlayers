package com.example.footballsystem.repositories;

import com.example.footballsystem.models.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findTeamByName(String name);
}
