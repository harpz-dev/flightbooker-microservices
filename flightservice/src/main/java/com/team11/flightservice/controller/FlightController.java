package com.team11.flightservice.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team11.flightservice.entity.Flight;
import com.team11.flightservice.service.FlightService;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> getFlights(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(required = false) LocalDate departureDate) {
        
        return ResponseEntity.ok(flightService.getAvailableFlights(source, destination, departureDate));
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<Flight> getFlightDetails(@PathVariable String flightNumber) {
        Optional<Flight> flight = flightService.getFlightDetails(flightNumber);
        return flight.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/sources")
    public ResponseEntity<List<String>> getAllSources() {
        return ResponseEntity.ok(flightService.getAllSources());
    }

    @GetMapping("/destinations")
    public ResponseEntity<List<String>> getAllDestinations() {
        return ResponseEntity.ok(flightService.getAllDestinations());
    }
}
