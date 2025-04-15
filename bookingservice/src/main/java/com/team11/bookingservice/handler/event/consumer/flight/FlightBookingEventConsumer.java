package com.team11.bookingservice.handler.event.consumer.flight;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.team11.bookingservice.dto.BookingEventResponseDTO;
import com.team11.bookingservice.exception.BookingException;
import com.team11.bookingservice.handler.event.consumer.EventConsumer;
import com.team11.bookingservice.service.BookingService;

@Component
public class FlightBookingEventConsumer implements EventConsumer {

    private final BookingService bookingService;

    public FlightBookingEventConsumer(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = "${booking.event.consumer.topics.FLIGHT}", groupId = "${booking.service.group.id}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(BookingEventResponseDTO event) throws BookingException {
        bookingService.handleResponse(event);
    }
}
