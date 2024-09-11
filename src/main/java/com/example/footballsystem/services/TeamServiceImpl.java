package com.example.footballsystem.services;

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
        List<Team> teams = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line = reader.readLine();

            try {
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");

                        String name = data[1];
                        String managerName = data[2];
                        String groupTeam = data[3];

                        if (EntityHelper.validateName(name) && EntityHelper.validateName(managerName)
                        && EntityHelper.validateGroup(groupTeam)) {
                            Team team = new Team();
                            team.setName(name);
                            team.setManagerName(managerName);
                            team.setGroupTeam(groupTeam);

                            Team team1 = repository.findTeamByName(name);
                            if(team1==null){
                                teams.add(team);
                            } else{
                                stringBuilder.append(String.format("A team with this name: %s already exists!%n",name));
                            }
                        }else {
                            stringBuilder.append(String.format(
                                    "Invalid parameters on one of the lines: " + line +"!"));
                        }
                }
                saveAll(teams);
            } catch (NumberFormatException | IndexOutOfBoundsException e){
                stringBuilder.append("Invalid parameters on one of the lines: " + line +"!").append(System.lineSeparator());
            }

        } catch (IOException e) {
            return "Invalid file!";
        }
        if (teams.isEmpty()) {
            return stringBuilder.toString();
        }
        return stringBuilder.append(String.format("You have successfully added %d teams", teams.size())).toString();

    }

    @Override
    public String delete(long id) {
     Team team = repository.findById(id)
             .orElseThrow(() -> new EntityNotFoundException(String.format("Team whit this %d does not exist!",id)));
     repository.delete(team);
     return "You have successfully deleted a team named: "+team.getName();
    }

    public void saveAll(List<Team> teams) {
       repository.saveAll(teams);
    }
}





