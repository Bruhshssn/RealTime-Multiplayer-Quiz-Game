package com.project.leaderboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardEntry {

    private int rank;
    private Long playerId;
    private String playerName;
    private double score;
}
