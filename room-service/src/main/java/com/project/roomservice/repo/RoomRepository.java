package com.project.roomservice.repo;

import com.project.roomservice.entity.GameRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    private static final String KEY_PREFIX = "room:";

    private final RedisTemplate<String, GameRoom> redisTemplate;

    long ttlMinutes= 30;

    private String key(String roomId) {
        return KEY_PREFIX + roomId;
    }

    public void save(GameRoom room) {
        redisTemplate.opsForValue().set(
                key(room.getRoomId()), room, ttlMinutes, TimeUnit.MINUTES
        );
    }

    public Optional<GameRoom> findById(String roomId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key(roomId)));
    }

    public void delete(String roomId) {
        redisTemplate.delete(key(roomId));
    }

    public List<GameRoom> findAll() {
        Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");
        if (keys == null || keys.isEmpty()) return List.of();
        return redisTemplate.opsForValue().multiGet(keys)
                .stream().filter(Objects::nonNull).toList();
    }
}
