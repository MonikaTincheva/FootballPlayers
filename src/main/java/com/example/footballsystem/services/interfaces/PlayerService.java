package com.example.footballsystem.services.interfaces;

import com.example.footballsystem.models.entity.Player;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayerService {
    List<Player> getAll();

    String processCSVFile(MultipartFile multipartFile);

    String delete(long id);

    String getPairOfPlayers();
}
