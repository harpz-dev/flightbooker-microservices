package com.team11.flightservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team11.flightservice.dto.BookingRequestDTO;
import com.team11.flightservice.entity.Flight;
import com.team11.flightservice.repository.FlightRepository;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAvailableFlights(String source, String destination, LocalDate departureDate) {
        if (departureDate != null) {
            return flightRepository.findBySourceIgnoreCaseAndDestinationIgnoreCaseAndDepartureDate(source, destination, departureDate);
        }
        return flightRepository.findBySourceAndDestinationAllIgnoreCase(source, destination);
    }

    public Optional<Flight> getFlightDetails(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Transactional
    public void updateSeatAvailability(String flightNumber, int bookedSeats) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        flight.updateAvailability(bookedSeats);
        flightRepository.save(flight);
    }

    /**
     * Processes a booking request by checking seat availability and updating the flight.
     * @param request The booking request DTO.
     * @return true if booking is successful, false otherwise.
     */
    @Transactional
    public boolean processBookingRequest(BookingRequestDTO request) {
        long flightId = request.getDetails().get("flightId");
        long numSeats = request.getDetails().get("numSeats");

        Optional<Flight> flightOptional = flightRepository.findById(flightId);
        if (flightOptional.isPresent()) {
            Flight flight = flightOptional.get();
            if (flight.getAvailableSeats() >= numSeats) {
                flight.updateAvailability((int) numSeats);
                flightRepository.save(flight);
                return true;
            }
        }
        return false;
    }

    public List<String> getAllSources() {
        return flightRepository.findDistinctSources();
    }

    public List<String> getAllDestinations() {
        return flightRepository.findDistinctDestinations();
    }
}
