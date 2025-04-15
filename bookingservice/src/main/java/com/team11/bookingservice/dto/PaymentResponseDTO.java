package com.team11.bookingservice.dto;

public class PaymentResponseDTO {
    private String bookingId;
    private double amount;
    private Long userId;

    public PaymentResponseDTO() {}

    public PaymentResponseDTO(String bookingId, double amount, Long userId) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.userId = userId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
