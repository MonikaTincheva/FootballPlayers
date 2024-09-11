package com.example.footballsystem.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 3, max = 30)
    @Column(unique = true)
    private String name;


    @NotEmpty
    @Size(min = 3, max = 30)
    private String managerName;


    @Size(min = 1, max = 2)
    private String groupTeam;


    @OneToMany(mappedBy = "team",fetch = FetchType.EAGER)
    private Set<Player> players = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return Objects.equals(id, team.id) && Objects.equals(name, team.name) && Objects.equals(managerName, team.managerName) && Objects.equals(groupTeam, team.groupTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, managerName, groupTeam);
    }
}
