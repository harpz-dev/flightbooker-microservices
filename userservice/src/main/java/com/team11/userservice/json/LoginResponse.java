package com.team11.userservice.json;

public class LoginResponse {

    private String accessToken;
    private String message;

    public LoginResponse(String accessToken, String message){
        this.accessToken = accessToken;
        this.message= message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
}
