package com.project.playerservice.controller;

import com.project.playerservice.dto.LoginRequest;
import com.project.playerservice.dto.RegisterRequest;
import com.project.playerservice.entity.Player;
import com.project.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    public ResponseEntity<Player> registerPlayer(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.createPlayer(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginPlayer(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(playerService.verifyPlayer(loginRequest));
    }
}
