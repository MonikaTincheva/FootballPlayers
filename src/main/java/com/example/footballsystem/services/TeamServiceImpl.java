package com.example.footballsystem.services;

import com.example.footballsystem.exceptions.EntityDuplicateException;
import com.example.footballsystem.helpers.EntityHelper;
import com.example.footballsystem.models.entity.Team;
import com.example.footballsystem.repositories.TeamRepository;
import com.example.footballsystem.services.interfaces.TeamService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    private TeamRepository repository;


    @Autowired
    public TeamServiceImpl(TeamRepository repository) {
        this.repository = repository;

    }


    @Override
    public List<Team> getAll() {
        return repository.findAll();
    }

    public String processCSVFile(MultipartFile file) {

        StringBuilder stringBuilder = new StringBuilder();
        int countTeam =0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line = reader.readLine();


            while ((line = reader.readLine()) != null) {

                try {
                    String[] data = line.split(",");
                    String name = data[1];
                    String managerName = data[2];
                    String groupTeam = data[3];

                    if (!EntityHelper.validateName(name) || !EntityHelper.validateName(managerName)
                            || !EntityHelper.validateGroup(groupTeam)) {
                        throw new NumberFormatException(data[1]);
                    }
                    if (repository.findTeamByName(name) != null) {
                        throw new EntityDuplicateException("Team", "name", name);
                    }

                    Team team = new Team(name, managerName, groupTeam);
                    repository.save(team);
                    countTeam++;


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
        if (countTeam==0) {
            return stringBuilder.toString();
        }
        return stringBuilder.append(String.format("You have successfully added %d teams", countTeam)).toString();

    }

    @Override
    public String delete(long id) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Team whit this %d does not exist!", id)));
        repository.delete(team);
        return "You have successfully deleted a team named: " + team.getName();
    }

}





