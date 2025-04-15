package com.team11.flightservice.service;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.team11.flightservice.dto.BookingRequestDTO;
import com.team11.flightservice.dto.BookingResponseDTO;
import com.team11.flightservice.entity.Flight;
import com.team11.flightservice.repository.FlightRepository;

@Service
public class BookingEventConsumer {
    private final FlightService flightService;
    private final BookingEventPublisher bookingEventPublisher;
    private final FlightRepository flightRepository;
    
    public BookingEventConsumer(FlightService flightService, BookingEventPublisher bookingEventPublisher, FlightRepository flightRepository) {
        this.flightService = flightService;
        this.bookingEventPublisher = bookingEventPublisher;
        this.flightRepository = flightRepository;
    }

    @KafkaListener(topics = "flight_booking_request", containerFactory = "bookingRequestKafkaListenerContainerFactory")
    public void consumeBookingRequest(BookingRequestDTO request) {
        System.out.println("Received booking request: " + request);

        boolean isBooked = flightService.processBookingRequest(request);
        
        long numOfSeats = request.getDetails().get("numSeats");
        long flightId = request.getDetails().get("flightId");
        double totalPrice = 0.0;

        if (isBooked) {
            Optional<Flight> flightOptional = flightRepository.findById(flightId);
            if (flightOptional.isPresent()) {
                Flight flight = flightOptional.get();
                totalPrice = flight.getPrice() * numOfSeats;
            }
        }
        BookingResponseDTO response = new BookingResponseDTO(request.getBookingId(), isBooked, totalPrice);
        bookingEventPublisher.publishBookingResponse(response); 
    }
}