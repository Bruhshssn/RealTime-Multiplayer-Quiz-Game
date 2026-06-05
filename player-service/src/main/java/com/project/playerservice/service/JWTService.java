package com.project.playerservice.service;

import com.project.playerservice.entity.Player;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private final String secretKey = "dWs2S1CjpEVkBc0REkAXSDhyxp6YurnDu8qE5ofguPc=";

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Player player) {
        return generateToken(player);
    }

    public String generateToken(Player player) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("playerId", player.getId());
        claims.put("playerName", player.getUsername());

        return Jwts.builder()
                .claims(claims)
                .subject(player.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = String.valueOf(extractUsername(token));

        return username.equals(userDetails.getUsername());
    }

}

