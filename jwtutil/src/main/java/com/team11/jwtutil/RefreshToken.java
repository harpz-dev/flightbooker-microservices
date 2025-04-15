package com.team11.jwtutil;

import org.springframework.core.env.Environment;

public class RefreshToken implements TokenType {

    private final String secret;
    private final long expirationTime = 1000 * 60 * 60 * 24 * 7; //7 days

    public RefreshToken(Environment env) {
        this.secret = env.getProperty("REFRESH_SECRET");
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public long getExpirationTime() {
        return expirationTime;
    }
}
