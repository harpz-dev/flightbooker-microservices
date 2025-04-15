package com.team11.bookingservice.handler.event.publisher;

import com.team11.bookingservice.dto.BookingEventDTO;
import com.team11.bookingservice.exception.BookingException;

public interface EventPublisher<E extends BookingEventDTO> {
    void publish(String topicKey, E event) throws BookingException;
}
