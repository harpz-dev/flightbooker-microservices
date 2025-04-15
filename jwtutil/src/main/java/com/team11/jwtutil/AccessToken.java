package com.team11.jwtutil;

import org.springframework.core.env.Environment;

public class AccessToken implements TokenType {

    private final String secret;
    private final long expirationTime = 1000 * 60 * 1 ;// 1 min expiry time (for each access token)

    public AccessToken(Environment env) {
        this.secret = env.getProperty("JWT_SECRET");
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
