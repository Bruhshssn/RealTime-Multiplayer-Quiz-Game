package com.project.leaderboardservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ScoreUpdateRequest {

    // roomId (UUID string) — identifies the game session for aggregated leaderboard
    private String roomId;

    private double points;
}