package com.team11.bookingservice.handler.event.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.team11.bookingservice.dto.PaymentResponseDTO;

@Component
public class PaymentEventPublisher {

    private final KafkaTemplate<String, PaymentResponseDTO> kafkaTemplate;

    public PaymentEventPublisher(KafkaTemplate<String, PaymentResponseDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentAmountResponse(PaymentResponseDTO responseDTO) {
        kafkaTemplate.send("payment-amount-response", responseDTO.getBookingId(), responseDTO);
    }
}
