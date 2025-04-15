package com.team11.bookingservice.handler.event.consumer;

import com.team11.bookingservice.dto.BookingEventResponseDTO;
import com.team11.bookingservice.exception.BookingException;

public interface EventConsumer {
    void consume(BookingEventResponseDTO event) throws BookingException;
}
