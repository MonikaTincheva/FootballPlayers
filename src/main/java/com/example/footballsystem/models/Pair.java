package com.example.footballsystem.models;

import com.example.footballsystem.models.entity.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Getter
@Setter
public class Pair {

    private Player playerOne;
    private Player playerTwo;


    public Pair(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair pair)) return false;
        return Objects.equals(playerOne, pair.playerOne) && Objects.equals(playerTwo, pair.playerTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerOne, playerTwo);
    }
}
