package com.team11.paymentservice.messaging;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.team11.paymentservice.dto.BookingResponseDTO;
import com.team11.paymentservice.dto.PaymentNotificationEventDTO;
import com.team11.paymentservice.entity.Payment;
import com.team11.paymentservice.enums.Status;
import com.team11.paymentservice.repository.PaymentRepository;

@Service
public class PaymentKafkaConsumer {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentNotificationEventPublisher eventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(PaymentKafkaConsumer.class);


    @KafkaListener(topics = "payment-amount-response", groupId = "payment-group", containerFactory = "amountResponseKafkaListenerContainerFactory")
    public void consumeAmount(BookingResponseDTO responseDTO) {
        Optional<Payment> optionalPayment = paymentRepository.findByBookingId(responseDTO.getBookingId());    
        if (optionalPayment.isEmpty()) {
            // handle missing payment
            logger.warn("Payment not found for bookingId: {}", responseDTO.getBookingId());

            return;
        }

        Payment payment = optionalPayment.get();
        if (responseDTO.getAmount() <= 0) {
            payment.setPaymentStatus(Status.FAILED);
            paymentRepository.save(payment);

            PaymentNotificationEventDTO event = new PaymentNotificationEventDTO(
                payment.getBookingId(),
                responseDTO.getUserId(), // ensure this is present
                payment.getPaymentStatus().name()
            );
            eventPublisher.publish(event);

            return;
        }
        
        payment.setAmount(responseDTO.getAmount());

        // Always succesfull for our case
        payment.setPaymentStatus(Status.SUCCESSFUL);

        PaymentNotificationEventDTO event = new PaymentNotificationEventDTO(
            payment.getBookingId(),
            responseDTO.getUserId(), // ensure this is present
            payment.getPaymentStatus().name()
        );
        eventPublisher.publish(event);


        paymentRepository.save(payment);
    }
}
