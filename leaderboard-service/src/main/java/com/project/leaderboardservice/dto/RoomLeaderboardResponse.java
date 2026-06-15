package com.project.leaderboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoomLeaderboardResponse {

    private String roomId;
    private List<LeaderboardEntry> entries;
}