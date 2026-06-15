package com.project.leaderboardservice.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class LeaderboardRepo {

    private final StringRedisTemplate redisTemplate;

    private Long keyPrefix = 100L;

    // ── Per-question key: "100:{questionId}" ──────────────────────────────────
    private String key(Long questionId) {
        return keyPrefix + ":" + questionId;
    }

    // ── Per-room key: "lb:room:{roomId}" ─────────────────────────────────────
    // Prefixed with "lb:" to avoid collision with room-service keys ("room:{roomId}")
    private String roomKey(String roomId) {
        return "lb:room:" + roomId;
    }

    // Add or update a player's score on a single question leaderboard
    public void addOrUpdateScore(Long questionId, Long playerId, String playerName, double score) {
        String member = playerId + ":" + playerName;
        redisTemplate.opsForZSet().add(key(questionId), member, score);
    }

    // Increment score atomically on the per-question leaderboard
    public Double incrementScore(Long questionId, Long playerId, String playerName, double delta) {
        String member = playerId + ":" + playerName;
        return redisTemplate.opsForZSet().incrementScore(key(questionId), member, delta);
    }

    /**
     * Increment score on the room-level aggregated leaderboard.
     * Called alongside incrementScore() so both keys stay in sync.
     */
    public Double incrementRoomScore(String roomId, Long playerId, String playerName, double delta) {
        String member = playerId + ":" + playerName;
        return redisTemplate.opsForZSet().incrementScore(roomKey(roomId), member, delta);
    }

    // Get top N players for a single question (highest score first)
    public Set<ZSetOperations.TypedTuple<String>> getTopN(Long questionId, int n) {
        return redisTemplate.opsForZSet()
                .reverseRangeWithScores(key(questionId), 0, n - 1);
    }

    // Get top N players for an entire room (aggregated across all questions)
    public Set<ZSetOperations.TypedTuple<String>> getRoomTopN(String roomId, int n) {
        return redisTemplate.opsForZSet()
                .reverseRangeWithScores(roomKey(roomId), 0, n - 1);
    }

    // Get a specific player's rank on a question leaderboard (1-indexed)
    public Long getPlayerRank(Long questionId, Long playerId, String playerName) {
        String member = playerId + ":" + playerName;
        Long rank = redisTemplate.opsForZSet().reverseRank(key(questionId), member);
        return rank != null ? rank + 1 : null;
    }

    // Get a specific player's rank on the room leaderboard (1-indexed)
    public Long getPlayerRoomRank(String roomId, Long playerId, String playerName) {
        String member = playerId + ":" + playerName;
        Long rank = redisTemplate.opsForZSet().reverseRank(roomKey(roomId), member);
        return rank != null ? rank + 1 : null;
    }

    // Get a specific player's score on a question leaderboard
    public Double getPlayerScore(Long questionId, Long playerId, String playerName) {
        return redisTemplate.opsForZSet().score(key(questionId), playerId + ":" + playerName);
    }

    // Delete a question's leaderboard
    public void deleteLeaderboard(Long questionId) {
        redisTemplate.delete(key(questionId));
    }

    // Delete a room's aggregated leaderboard
    public void deleteRoomLeaderboard(String roomId) {
        redisTemplate.delete(roomKey(roomId));
    }
}