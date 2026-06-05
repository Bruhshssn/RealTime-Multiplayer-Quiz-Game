package com.project.playerservice.repo;

import com.project.playerservice.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepo extends JpaRepository<Player, Long> {
    Optional<Player> findByEmail(String email);

    Player getPlayerByEmail(String email);
}
