package com.project.leaderboardservice.controller;

import com.project.leaderboardservice.dto.LeaderboardEntry;
import com.project.leaderboardservice.dto.LeaderboardResponse;
import com.project.leaderboardservice.dto.RoomLeaderboardResponse;
import com.project.leaderboardservice.dto.ScoreUpdateRequest;
import com.project.leaderboardservice.service.JwtAuthFilter;
import com.project.leaderboardservice.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @PostMapping("/{questionId}/score")
    public ResponseEntity<LeaderboardEntry> submitScore(@PathVariable Long questionId, @RequestBody ScoreUpdateRequest request, Authentication authentication) {

        JwtAuthFilter.PlayerPrincipal principal =
                (JwtAuthFilter.PlayerPrincipal) authentication.getPrincipal();

        LeaderboardEntry entry = leaderboardService.submitScore(
                questionId,
                principal.playerId(),
                principal.playerName(),
                request.getPoints(),
                request.getRoomId()   // roomId — the game session identifier
        );
        return ResponseEntity.ok(entry);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<LeaderboardResponse> getLeaderboard(
            @PathVariable Long questionId,
            @RequestParam(defaultValue = "10") int topN) {

        return ResponseEntity.ok(leaderboardService.getLeaderboard(questionId, topN));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomLeaderboardResponse> getRoomLeaderboard(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "20") int topN) {

        return ResponseEntity.ok(leaderboardService.getRoomLeaderboard(roomId, topN));
    }
}