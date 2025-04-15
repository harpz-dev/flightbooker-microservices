package com.team11.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.team11.notificationservice.dto.PaymentEventDTO;
import com.team11.notificationservice.service.strategy.NotificationStrategy;

@Service
public class NotificationService {

    private final UserServiceClient userServiceClient;
    private final NotificationStrategy notificationStrategy;
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(UserServiceClient userServiceClient, NotificationStrategy notificationStrategy) {
        this.userServiceClient = userServiceClient;
        this.notificationStrategy = notificationStrategy;
    }

    public void handleNotification(PaymentEventDTO event) {
        logger.info("Received booking event for user: {}, bookingId: {}", event.getUserId(), event.getBookingId());

        try {
            String userEmail = userServiceClient.getUserById(event.getUserId());

            if (userEmail != null && !userEmail.isEmpty()) {
                String message = "Your booking with ID " + event.getBookingId() + " is " + event.getStatus() + ".";
                notificationStrategy.send(userEmail, message);
                logger.info("Notification sent to {}", userEmail);
            } else {
                logger.warn("User email not found for userId {}", event.getUserId());
            }
            
            // String message = "Your booking with ID " + event.getBookingId() + " is " + event.getStatus() + ".";
            // notificationStrategy.send("brahmpreet.singh@unb.ca", message);
            // logger.info("Notification sent to {}", "brahmpreet.singh@unb.ca");
           
        } catch (Exception ex) {
            logger.error("Failed to send notification for bookingId: {}", event.getBookingId(), ex);
        }
    }
}
