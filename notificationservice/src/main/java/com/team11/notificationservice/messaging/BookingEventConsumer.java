package com.team11.notificationservice.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.team11.notificationservice.dto.PaymentEventDTO;
import com.team11.notificationservice.service.NotificationService;

@Component
public class BookingEventConsumer {

    private final NotificationService notificationService;

    public BookingEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "payment-events", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, PaymentEventDTO> record) {
        notificationService.handleNotification(record.value());
    }
}
