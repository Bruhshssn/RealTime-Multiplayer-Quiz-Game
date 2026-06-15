package com.project.leaderboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LeaderboardResponse {

    private Long questionId;
    private List<LeaderboardEntry> entries;
}
