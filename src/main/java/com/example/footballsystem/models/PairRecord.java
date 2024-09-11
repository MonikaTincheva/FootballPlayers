package com.example.footballsystem.models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
public class PairRecord {

    int totalTime;

    Map<Long, Integer> matchesRecord;

    public PairRecord( Long matchId,int totalTime) {
        this.totalTime = totalTime;
        matchesRecord = new HashMap<>();
        matchesRecord.put(matchId,totalTime);
    }
}
