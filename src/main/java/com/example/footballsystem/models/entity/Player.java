package com.example.footballsystem.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Objects;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "players")

public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 3, max = 30)
    private String fullName;

    @NotNull
    @Min(value = 1, message = "Team number must be at least 1")
    @Max(value = 99, message = "Team number must be no more than 99")
    private Integer teamNumber;

    @Size(min = 1, max = 2)
    private String position;

    @ManyToOne
    @JoinColumn()
    private Team team;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return Objects.equals(fullName, player.fullName) && Objects.equals(teamNumber, player.teamNumber) && Objects.equals(position, player.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, teamNumber, position);
    }
}
