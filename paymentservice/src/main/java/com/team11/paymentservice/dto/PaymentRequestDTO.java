package com.team11.paymentservice.dto;

import com.team11.paymentservice.enums.PaymentType;

public class PaymentRequestDTO {
    private PaymentType method;
    private String bookingId;

    // Getters and setters
    public String getBookingId() {
        return bookingId;
    }

    public PaymentType getMethod() {
        return method;
    }

    public void setMethod(PaymentType method) {
        this.method = method;
    }

    public void setBookingId(String string) {
        this.bookingId = string; 
    }
}