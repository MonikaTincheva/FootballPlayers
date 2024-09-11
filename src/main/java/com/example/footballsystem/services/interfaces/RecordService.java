package com.example.footballsystem.services.interfaces;

import com.example.footballsystem.models.entity.Record;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecordService {
    List<Record> getAll();

    String processCSVFile(MultipartFile multipartFile);

    String delete(long id);


}
