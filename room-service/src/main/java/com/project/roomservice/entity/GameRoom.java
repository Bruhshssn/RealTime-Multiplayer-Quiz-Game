package com.project.roomservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom implements Serializable {

    private String roomId;
    private Long hostId;
    private String hostName;
    private int maxPlayers;
    private RoomStatus status;
    private Map<Long, String> players;
    private LocalDateTime createdAt;

    public enum RoomStatus {
        WAITING, IN_PROGRESS, CLOSED
    }
}
