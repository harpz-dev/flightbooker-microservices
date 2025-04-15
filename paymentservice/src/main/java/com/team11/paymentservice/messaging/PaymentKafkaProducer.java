package com.team11.paymentservice.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public PaymentKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void requestAmount(String bookingId) {
        kafkaTemplate.send("payment-amount-request", bookingId);
    }
}