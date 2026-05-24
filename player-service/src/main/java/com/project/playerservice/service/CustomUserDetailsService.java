package com.project.playerservice.service;


import com.project.playerservice.entity.Player;
import com.project.playerservice.repo.PlayerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final PlayerRepo playerRepo;

    public CustomUserDetailsService(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Player player = playerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(player.getEmail())
                .password(player.getPassword())
                .build();
    }
}

