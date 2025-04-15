package com.team11.flightservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team11.flightservice.dto.BookingRequestDTO;
import com.team11.flightservice.dto.BookingResponseDTO;
import com.team11.flightservice.entity.Flight;
import com.team11.flightservice.repository.FlightRepository;
import com.team11.flightservice.service.BookingEventConsumer;
import com.team11.flightservice.service.BookingEventPublisher;
import com.team11.flightservice.service.FlightService;

@ExtendWith(MockitoExtension.class)
class BookingEventConsumerSpringTest {

    @Mock
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private BookingEventPublisher bookingEventPublisher;

    // We'll instantiate BookingEventConsumer manually
    private BookingEventConsumer bookingEventConsumer;

    @BeforeEach
    void setup() {
        // We "new" it ourselves, injecting the mocks
        bookingEventConsumer = new BookingEventConsumer(
            flightService,
            bookingEventPublisher,
            flightRepository
        );
    }

    @Test
    void testConsumeBookingRequest_Success() {
        UUID bookingId = UUID.randomUUID();
        BookingRequestDTO request = new BookingRequestDTO(
            bookingId,
            Map.of("flightId", 1L, "numSeats", 2L)
        );

        Flight flight = new Flight();
        flight.setPrice(150.0);

        when(flightService.processBookingRequest(any())).thenReturn(true);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        bookingEventConsumer.consumeBookingRequest(request);

        ArgumentCaptor<BookingResponseDTO> captor = ArgumentCaptor.forClass(BookingResponseDTO.class);
        verify(bookingEventPublisher).publishBookingResponse(captor.capture());

        BookingResponseDTO actual = captor.getValue();
        Assertions.assertEquals(bookingId, actual.getBookingId());
        Assertions.assertTrue(actual.isSuccessful());
        Assertions.assertEquals(300.0, actual.getAmount(), 0.0001);
    }


    @Test
    void testConsumeBookingRequest_FlightNotFound() {
        UUID bookingId = UUID.randomUUID();
        BookingRequestDTO request = new BookingRequestDTO(
            bookingId,
            Map.of("flightId", 999L, "numSeats", 2L)
        );

        when(flightService.processBookingRequest(any())).thenReturn(true);
        when(flightRepository.findById(999L)).thenReturn(Optional.empty());

        bookingEventConsumer.consumeBookingRequest(request);

        // Should publish success but amount=0.0
        ArgumentCaptor<BookingResponseDTO> captor = ArgumentCaptor.forClass(BookingResponseDTO.class);
        verify(bookingEventPublisher).publishBookingResponse(captor.capture());
        BookingResponseDTO actual = captor.getValue();

        Assertions.assertEquals(bookingId, actual.getBookingId());
        Assertions.assertTrue(actual.isSuccessful());
        Assertions.assertEquals(0.0, actual.getAmount(), 0.0001);
    }
}