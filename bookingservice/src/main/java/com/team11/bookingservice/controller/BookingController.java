package com.team11.bookingservice.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import com.team11.bookingservice.dto.BookingResponseDTO;
import com.team11.bookingservice.entity.BookingEntity;
import com.team11.bookingservice.enums.Status;
import com.team11.bookingservice.exception.BookingException;
import com.team11.bookingservice.exception.UnauthorizedException;
import com.team11.bookingservice.dto.BookingRequestModel;
import com.team11.bookingservice.service.BookingService;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Receives a booking request from the frontend.
     * Expects a JWT in the Authorization header.
     *
     * @throws BookingException
     */
    @PostMapping
    public ResponseEntity<?> processRequest(
        @RequestBody BookingRequestModel requestModel,
        HttpServletRequest request
    ) throws BookingException, UnauthorizedException {
        // Extract JWT from header and retrieve userId
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        Long userId = null;
        try {
            userId = bookingService.extractUserId(token);
        } catch (Exception e) {
            System.err.println(this.getClass().getSimpleName() + ": " + e.getMessage() + "\n" + e.getStackTrace());
            throw e;
        }

        // Long userId = Long.parseLong("1234");

        UUID bookingId = bookingService.createBooking(requestModel, userId).getBookingId();

        //failed or successful..
        log.debug("End of processRequest method");
        return new ResponseEntity<>(new BookingResponseDTO(bookingId), HttpStatus.ACCEPTED);
    }

    // TODO getmapping
    // returns all bookings for userId extracted from jwt
    // 

    /**
     * Polling endpoint for the frontend to retrieve booking status.
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingByBookingId(@PathVariable UUID bookingId) {
        Optional<BookingEntity> booking = bookingService.findByBookingId(bookingId);
        if (booking.isEmpty()) {
            log.debug("getBookingByBookingId: booking with provided bookingId not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.debug("End of processRequest method");
        return new ResponseEntity<>(booking.get(), HttpStatus.OK);
    }

   @GetMapping("/status/{bookingId}")
    public ResponseEntity<String> getBookingStatus(@PathVariable("bookingId") UUID bookingId) {
        
        // Fetch the booking status
        Status status = bookingService.getBookingStatus(bookingId);

        if (status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
        }

        return ResponseEntity.ok(status.toString());
    }

    @GetMapping("/history")
    public ResponseEntity<?> getBookingHistory(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        Long userId;
        try {
            userId = bookingService.extractUserId(token);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Fetch only successful bookings for this user
        List<BookingEntity> successfulBookings = bookingService.getSuccessfulBookingsByUserId(userId);
        return new ResponseEntity<>(successfulBookings, HttpStatus.OK);
    }
}
