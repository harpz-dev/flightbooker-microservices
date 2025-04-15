package com.team11.bookingservice.handler.event.consumer.payment;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.team11.bookingservice.dto.PaymentNotificationEventDTO;
import com.team11.bookingservice.service.BookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final BookingService bookingService;

    @KafkaListener(topics = "payment-events", groupId = "booking-service-group", containerFactory = "paymentNotificationKafkaListenerContainerFactory")
    public void consume(PaymentNotificationEventDTO event) {
        log.info("Received payment event for bookingId: {}, status: {}", event.getBookingId(), event.getStatus());

        try {
            UUID bookingId = UUID.fromString(event.getBookingId());
            String status = event.getStatus();

            if ("SUCCESSFUL".equalsIgnoreCase(status)) {
                bookingService.markBookingAsSuccessful(bookingId);
                log.info("Booking marked as successful for ID: {}", bookingId);
            } else {
                log.warn("Unhandled payment status: {}", status);
            }
        } catch (Exception e) {
            log.error("Error while updating booking status", e);
        }
    }
}
