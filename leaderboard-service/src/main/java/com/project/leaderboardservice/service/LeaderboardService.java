package com.project.leaderboardservice.service;

import com.project.leaderboardservice.dto.LeaderboardEntry;
import com.project.leaderboardservice.dto.LeaderboardResponse;
import com.project.leaderboardservice.dto.RoomLeaderboardResponse;
import com.project.leaderboardservice.repo.LeaderboardRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final LeaderboardRepo repository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Submit a score for a specific question.
     * Also increments the room-level aggregated leaderboard if roomId is provided.
     * Broadcasts live updates on both per-question and per-room WebSocket topics.
     */
    public LeaderboardEntry submitScore(Long questionId, Long playerId,
                                        String playerName, double points, String roomId) {

        // 1. Update per-question leaderboard (existing behaviour)
        Double newQuestionScore = repository.incrementScore(questionId, playerId, playerName, points);
        Long questionRank = repository.getPlayerRank(questionId, playerId, playerName);

        // 2. Update room-level aggregated leaderboard
        if (roomId != null && !roomId.isBlank()) {
            repository.incrementRoomScore(roomId, playerId, playerName, points);

            // Broadcast live room leaderboard to WebSocket subscribers
            broadcastRoomLeaderboard(roomId);
        }

        // 3. Broadcast per-question leaderboard
        broadcastLeaderboard(questionId);

        return new LeaderboardEntry(questionRank.intValue(), playerId, playerName, newQuestionScore);
    }

    // ── Per-question leaderboard ──────────────────────────────────────────────

    public LeaderboardResponse getLeaderboard(Long questionId, int topN) {
        Set<ZSetOperations.TypedTuple<String>> entries =
                repository.getTopN(questionId, topN);

        List<LeaderboardEntry> result = buildEntries(entries);
        return new LeaderboardResponse(questionId, result);
    }

    // ── Room-level aggregated leaderboard ─────────────────────────────────────

    public RoomLeaderboardResponse getRoomLeaderboard(String roomId, int topN) {
        Set<ZSetOperations.TypedTuple<String>> entries =
                repository.getRoomTopN(roomId, topN);

        List<LeaderboardEntry> result = buildEntries(entries);
        return new RoomLeaderboardResponse(roomId, result);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private List<LeaderboardEntry> buildEntries(Set<ZSetOperations.TypedTuple<String>> raw) {
        List<LeaderboardEntry> result = new ArrayList<>();
        int rank = 1;
        for (ZSetOperations.TypedTuple<String> entry : raw) {
            String[] parts = entry.getValue().split(":", 2);
            Long playerId = Long.parseLong(parts[0]);
            String playerName = parts[1];
            result.add(new LeaderboardEntry(rank++, playerId, playerName, entry.getScore()));
        }
        return result;
    }

    private void broadcastLeaderboard(Long questionId) {
        LeaderboardResponse board = getLeaderboard(questionId, 10);
        messagingTemplate.convertAndSend("/topic/leaderboard/" + questionId, board);
    }

    private void broadcastRoomLeaderboard(String roomId) {
        RoomLeaderboardResponse board = getRoomLeaderboard(roomId, 20);
        // Frontend can subscribe to /topic/leaderboard/room/{roomId} for live updates
        messagingTemplate.convertAndSend("/topic/leaderboard/room/" + roomId, board);
    }
}