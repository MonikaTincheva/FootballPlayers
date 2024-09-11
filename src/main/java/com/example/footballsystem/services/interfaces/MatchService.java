package com.example.footballsystem.services.interfaces;

import com.example.footballsystem.models.entity.Match;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MatchService {
    List<Match> getAll();

    String processCSVFile(MultipartFile file);

    String delete(long id);
}
