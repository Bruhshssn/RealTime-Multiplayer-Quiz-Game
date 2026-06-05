package com.project.roomservice.service;

import com.project.roomservice.entity.GameRoom;
import com.project.roomservice.exceptions.RoomException;
import com.project.roomservice.repo.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public GameRoom createRoom(Long hostId, String hostName,
                               String roomName, int maxPlayers) {
        // One host = one active room (optional rule)
        roomRepository.findAll().stream()
                .filter(r -> r.getHostId().equals(hostId)
                        && r.getStatus() == GameRoom.RoomStatus.WAITING)
                .findFirst()
                .ifPresent(r -> { throw new RoomException("You already have an open room"); });

        Map<Long, String> players = new LinkedHashMap<>();
        players.put(hostId, hostName); // host auto-joins

        GameRoom room = GameRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .hostId(hostId)
                .hostName(hostName)
                .maxPlayers(maxPlayers)
                .status(GameRoom.RoomStatus.WAITING)
                .players(players)
                .createdAt(LocalDateTime.now())
                .build();

        roomRepository.save(room);
        return room;
    }

    public GameRoom joinRoom(String roomId, Long playerId, String playerName) {
        GameRoom room = getRoom(roomId);

        if (room.getStatus() != GameRoom.RoomStatus.WAITING)
            throw new RoomException("Room is not accepting players");
        if (room.getPlayers().size() >= room.getMaxPlayers())
            throw new RoomException("Room is full");
        if (room.getPlayers().containsKey(playerId))
            throw new RoomException("Already in this room");

        room.getPlayers().put(playerId, playerName);
        roomRepository.save(room);
        return room;
    }

    public GameRoom leaveRoom(String roomId, Long playerId) {
        GameRoom room = getRoom(roomId);

        if (!room.getPlayers().containsKey(playerId))
            throw new RoomException("Player not in this room");

        room.getPlayers().remove(playerId);

        if (room.getPlayers().isEmpty()) {
            roomRepository.delete(roomId); // last one out, close the room
            return null;
        }

        // Transfer host if host left
        if (room.getHostId().equals(playerId)) {
            Map.Entry<Long, String> newHost = room.getPlayers().entrySet().iterator().next();
            room.setHostId(newHost.getKey());
            room.setHostName(newHost.getValue());
        }

        roomRepository.save(room);
        return room;
    }

    public GameRoom closeRoom(String roomId, Long requesterId) {
        GameRoom room = getRoom(roomId);
        if (!room.getHostId().equals(requesterId))
            throw new RoomException("Only the host can close the room");
        room.setStatus(GameRoom.RoomStatus.CLOSED);
        roomRepository.save(room);
        return room;
    }

    public GameRoom getRoom(String roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException("Room not found: " + roomId));
    }

    public List<GameRoom> getOpenRooms() {
        return roomRepository.findAll().stream()
                .filter(r -> r.getStatus() == GameRoom.RoomStatus.WAITING)
                .toList();
    }
}