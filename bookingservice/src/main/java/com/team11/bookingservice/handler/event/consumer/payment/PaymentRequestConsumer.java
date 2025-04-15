package com.team11.bookingservice.handler.event.consumer.payment;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.team11.bookingservice.dto.PaymentResponseDTO;
import com.team11.bookingservice.exception.BookingException;
import com.team11.bookingservice.handler.event.publisher.PaymentEventPublisher;
import com.team11.bookingservice.service.BookingService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentRequestConsumer {

    private final BookingService bookingService;
    private final PaymentEventPublisher paymentEventPublisher;
    

    public PaymentRequestConsumer(BookingService bookingService, PaymentEventPublisher paymentEventPublisher) {
        this.bookingService = bookingService;
        this.paymentEventPublisher = paymentEventPublisher;
    }

    @KafkaListener(
        topics = "payment-amount-request",
        groupId = "booking-payment-consumer-group",
        containerFactory = "paymentRequestKafkaListenerContainerFactory"
    )
    public void consumeBookingId(String bookingId) throws BookingException {
        double amount = bookingService.getAmountByBookingId(bookingId);
        Long userId = bookingService.getUserIdByBookingId(bookingId);
        
        log.info("Booking found for user id....." + userId);

        PaymentResponseDTO responseDTO = new PaymentResponseDTO(bookingId, amount, userId);
        paymentEventPublisher.publishPaymentAmountResponse(responseDTO);
    }
}