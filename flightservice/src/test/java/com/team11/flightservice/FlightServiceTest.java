package com.team11.flightservice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team11.flightservice.entity.Flight;
import com.team11.flightservice.repository.FlightRepository;
import com.team11.flightservice.service.FlightService;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void testGetAvailableFlights_WithDate() {
        List<Flight> expectedFlights = List.of(new Flight());
        Mockito.when(flightRepository
                .findBySourceIgnoreCaseAndDestinationIgnoreCaseAndDepartureDate("Delhi", "Toronto", LocalDate.of(2025, 4, 1)))
                .thenReturn(expectedFlights);

        List<Flight> actual = flightService.getAvailableFlights("Delhi", "Toronto", LocalDate.of(2025, 4, 1));
        Assertions.assertEquals(expectedFlights, actual);
    }

    @Test
    void testUpdateSeatAvailability_Success() {
        Flight flight = new Flight();
        flight.setAvailableSeats(10);

        Mockito.when(flightRepository.findByFlightNumber("FL123")).thenReturn(Optional.of(flight));
        flightService.updateSeatAvailability("FL123", 3);

        Assertions.assertEquals(7, flight.getAvailableSeats());
        Mockito.verify(flightRepository).save(flight);
    }

    @Test
    void testUpdateSeatAvailability_ThrowsIfSeatsUnavailable() {
        Flight flight = new Flight();
        flight.setAvailableSeats(2);

        Mockito.when(flightRepository.findByFlightNumber("FL999")).thenReturn(Optional.of(flight));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            flightService.updateSeatAvailability("FL999", 5);
        });
    }
}
