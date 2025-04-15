package com.team11.paymentservice.service;

import com.team11.paymentservice.dto.PaymentRequestDTO;
import com.team11.paymentservice.entity.Payment;
import com.team11.paymentservice.enums.Status;
import com.team11.paymentservice.messaging.PaymentKafkaProducer;
import com.team11.paymentservice.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final PaymentKafkaProducer paymentKafkaProducer;
        
    
    public PaymentService(PaymentRepository paymentRepository, PaymentKafkaProducer paymentKafkaProducer) {
        this.paymentRepository = paymentRepository;
        this.paymentKafkaProducer = paymentKafkaProducer;
    }

    @Transactional
    public Payment processPayment(PaymentRequestDTO paymentRequest) {
        Payment payment = new Payment();
        payment.setBookingId(paymentRequest.getBookingId());
        payment.setPaymentType(paymentRequest.getMethod());
        payment.setPaymentStatus(Status.PENDING);
        paymentRepository.save(payment);

        paymentKafkaProducer.requestAmount(paymentRequest.getBookingId());
        logger.info("Pending payment created and amount request from booking service {}", payment.getId());

        return payment;
    }

    public Optional<Payment> getPaymentDetails(Long id) {
        return paymentRepository.findById(id);
    }
}