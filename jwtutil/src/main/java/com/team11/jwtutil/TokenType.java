package com.team11.jwtutil;

public interface TokenType {

    public String getSecret();
    public long getExpirationTime();

}