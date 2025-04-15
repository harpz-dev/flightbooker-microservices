package com.team11.notificationservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.service.url}")
    private String userServiceBaseUrl; //this is not used yet

    public String getUserById(Long userId) {
        return restTemplate.getForObject("http://userservice:8083/api/auth/get-email?userId=" + userId, String.class);
    }
}