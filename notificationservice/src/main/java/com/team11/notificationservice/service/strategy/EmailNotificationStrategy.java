package com.team11.notificationservice.service.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationStrategy implements NotificationStrategy {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationStrategy.class);

    public EmailNotificationStrategy(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String email, String message) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(email);
            mail.setSubject("Booking Confirmed");
            mail.setText(message);
            mailSender.send(mail);

            logger.info("Email sent to {}", email);
        } catch (Exception e) {
            logger.error("Failed to send email to {}", email, e);
        }
    }
}
