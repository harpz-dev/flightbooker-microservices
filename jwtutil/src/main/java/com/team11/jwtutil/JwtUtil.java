package com.team11.jwtutil;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.time.Instant;
import javax.crypto.SecretKey;

public class JwtUtil {

    private final String secret;
    private final long expirationTime;

    //Strategy pattern
    public JwtUtil(TokenType tokenType) {
        this.secret = tokenType.getSecret();
        this.expirationTime = tokenType.getExpirationTime();
    }

    public String generateToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expirationTime)))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new SecurityException("JWT secret key must be at least 32 bytes long.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build();
    }

    public boolean validateToken(String token) {
        try {
            getJwtParser().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    public String extractUserIdString(String token) {
        return getJwtParser().parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}