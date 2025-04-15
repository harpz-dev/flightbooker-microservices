package com.team11.paymentservice.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team11.paymentservice.dto.PaymentRequestDTO;
import com.team11.paymentservice.entity.Payment;
import com.team11.paymentservice.service.PaymentService;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<Long> processPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        Payment payment = paymentService.processPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(payment.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentDetails(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentDetails(id);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
