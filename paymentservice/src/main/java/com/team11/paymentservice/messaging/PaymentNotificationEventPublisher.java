package com.team11.paymentservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.team11.paymentservice.dto.PaymentNotificationEventDTO;

@Component
public class PaymentNotificationEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(PaymentNotificationEventPublisher.class);

    private final KafkaTemplate<String, PaymentNotificationEventDTO> kafkaTemplate;

    public PaymentNotificationEventPublisher(KafkaTemplate<String, PaymentNotificationEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(PaymentNotificationEventDTO event) {
        kafkaTemplate.send("payment-events", event.getBookingId(), event);
        logger.info("📤 Published payment notification event for bookingId={}, userId={}, status={}",
                event.getBookingId(), event.getUserId(), event.getStatus());
    }
}
