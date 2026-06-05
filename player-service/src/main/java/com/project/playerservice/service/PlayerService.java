package com.project.playerservice.service;

import com.project.playerservice.dto.LoginRequest;
import com.project.playerservice.dto.RegisterRequest;
import com.project.playerservice.dto.ResetPasswordDTO;
import com.project.playerservice.entity.Player;
import com.project.playerservice.repo.PlayerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepo playerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    public Player createPlayer(RegisterRequest registerRequest) {
        Player player = new Player();

        player.setUsername(registerRequest.getUsername());
        player.setEmail(registerRequest.getEmail());
        player.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        return playerRepo.save(player);
    }

    public String verifyPlayer(LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            Player player = playerRepo.getPlayerByEmail(loginRequest.getEmail());
            return jwtService.generateToken(player);
        }
        return "Failed to verify player";
    }

    public Player updatePassword(ResetPasswordDTO resetPasswordDTO) {
        Player player = playerRepo.getPlayerByEmail(resetPasswordDTO.getEmail());

        player.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        return playerRepo.save(player);
    }

    public Player getPlayerById(Long id) {
        return playerRepo.findById(id).get();
    }
}
