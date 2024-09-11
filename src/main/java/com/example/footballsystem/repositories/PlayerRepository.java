package com.example.footballsystem.repositories;

import com.example.footballsystem.models.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findPlayerByFullNameAndAndPosition(String fullName, String position);
}
