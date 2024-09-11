package com.example.footballsystem.models.entity;

import com.example.footballsystem.models.entity.Match;
import com.example.footballsystem.models.entity.Player;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @NotNull
    @Min(value = 0)
    @Max(value = 90)
    private int fromMinutes;

    @NotNull
    @Min(value = 1)
    @Max(value = 90)
    private int toMinutes;

    public Record(Player player, Match match, int fromMinutes, int toMinutes) {
        this.player = player;
        this.match = match;
        this.fromMinutes = fromMinutes;
        this.toMinutes = toMinutes;
    }
}
