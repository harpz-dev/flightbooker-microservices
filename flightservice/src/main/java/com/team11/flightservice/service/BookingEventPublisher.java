package com.team11.flightservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.team11.flightservice.dto.BookingResponseDTO;

@Service
public class BookingEventPublisher {

    private final KafkaTemplate<String, BookingResponseDTO> kafkaTemplate;
    private static final String BOOKING_RESPONSE_TOPIC = "flight_booking_response";

    public BookingEventPublisher(KafkaTemplate<String, BookingResponseDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishBookingResponse(BookingResponseDTO response) {
        kafkaTemplate.send(BOOKING_RESPONSE_TOPIC, response);
    }
}
