package com.team11.paymentservice.entity;
import java.time.Instant;

import com.team11.paymentservice.enums.PaymentType;
import com.team11.paymentservice.enums.Status;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingId;

    @Column(nullable = false)
    private double amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING) // Stores the enum as string values in the db
    @Column(nullable = false)
    private Status paymentStatus = Status.PENDING; // Default to PENDING

    @Column(nullable = false)
    private Instant time;

    public Payment() {
        this.time = Instant.now();
    }

    public Payment(String bookingId, PaymentType paymentType) {
        this.bookingId = bookingId;
        this.paymentType = paymentType;
        this.time = Instant.now();
    }

    public Long getId() { return id; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Status getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(Status paymentStatus) { this.paymentStatus = paymentStatus; }
    public PaymentType getPaymentType() { return paymentType; }
    public void setPaymentType(PaymentType paymentType) { this.paymentType = paymentType; }

}