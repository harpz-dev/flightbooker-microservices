package com.team11.flightservice.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team11.flightservice.entity.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumber(String flightNumber);
    
    List<Flight> findBySourceAndDestinationAllIgnoreCase(String source, String destination);

    List<Flight> findBySourceIgnoreCaseAndDestinationIgnoreCaseAndDepartureDate(
            String source, String destination, LocalDate departureDate);

    List<Flight> findAll();

    @Query("SELECT DISTINCT f.source FROM Flight f")
    List<String> findDistinctSources();

    @Query("SELECT DISTINCT f.destination FROM Flight f")
    List<String> findDistinctDestinations();
}
