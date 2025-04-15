package com.team11.notificationservice.service.strategy;

public interface NotificationStrategy {
    void send(String email, String message);
}
