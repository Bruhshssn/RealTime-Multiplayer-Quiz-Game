package com.project.playerservice.controller;

import com.project.playerservice.dto.LoginRequest;
import com.project.playerservice.dto.RegisterRequest;
import com.project.playerservice.dto.ResetPasswordDTO;
import com.project.playerservice.entity.Player;
import com.project.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/resetPass")
    public ResponseEntity<Player> PasswordReset(@RequestBody ResetPasswordDTO resetPasswordDTO){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(playerService.updatePassword(resetPasswordDTO));
    }

    @GetMapping("/getPlayerById/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(playerService.getPlayerById(id));
    }
}
