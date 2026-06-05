package com.project.roomservice.controller;

import com.project.roomservice.entity.GameRoom;
import com.project.roomservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    private Long currentPlayerId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private String currentPlayerName() {
        return (String) ((UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication())
                .getDetails();
    }

    @PostMapping
    public ResponseEntity<GameRoom> createRoom(@RequestBody CreateRoomRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(currentPlayerId(), currentPlayerName(),
                        req.roomName(), req.maxPlayers())
        );
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<GameRoom> joinRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.joinRoom(roomId, currentPlayerId(), currentPlayerName()));
    }

    @DeleteMapping("/{roomId}/leave")
    public ResponseEntity<GameRoom> leaveRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.leaveRoom(roomId, currentPlayerId()));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<GameRoom> closeRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.closeRoom(roomId, currentPlayerId()));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<GameRoom> getRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.getRoom(roomId));
    }

    @GetMapping
    public ResponseEntity<List<GameRoom>> getOpenRooms() {
        return ResponseEntity.ok(roomService.getOpenRooms());
    }

    // DTOs
    public record CreateRoomRequest(String roomName, int maxPlayers) {}
}
