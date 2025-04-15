package com.team11.paymentservice.dto;

public class PaymentNotificationEventDTO {
    private String bookingId;
    private Long userId;
    private String status;

    public PaymentNotificationEventDTO() {}

    public PaymentNotificationEventDTO(String bookingId, Long userId, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
