package com.example.footballsystem.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Team ATeams;

    @NotNull
    @ManyToOne
    private Team BTeams;

    private LocalDate date;

    @Size(min = 2, max = 10)
    private String score;


    @OneToMany(mappedBy = "match", cascade = CascadeType.DETACH, orphanRemoval = true)
    private Set<Record> records = new HashSet<>();

    public Match(Team ATeams, Team BTeams, LocalDate date, String score) {
        this.ATeams = ATeams;
        this.BTeams = BTeams;
        this.date = date;
        this.score = score;
        records = new HashSet<>();
    }
}
