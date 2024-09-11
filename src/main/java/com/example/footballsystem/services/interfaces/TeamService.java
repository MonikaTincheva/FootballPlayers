package com.example.footballsystem.services.interfaces;

import com.example.footballsystem.models.entity.Team;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeamService {
    List<Team> getAll();

    String processCSVFile(MultipartFile multipartFile);

    String delete(long id);

}
