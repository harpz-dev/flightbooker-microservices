package com.team11.bookingservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team11.bookingservice.dto.BookingEventDTO;
import com.team11.bookingservice.dto.BookingEventResponseDTO;
import com.team11.bookingservice.dto.BookingRequestModel;
import com.team11.bookingservice.entity.BookingEntity;
import com.team11.bookingservice.enums.Status;
import com.team11.bookingservice.exception.BookingException;
import com.team11.bookingservice.exception.UnauthorizedException;
import com.team11.bookingservice.handler.event.publisher.BookingEventPublisher;
import com.team11.bookingservice.mapper.BookingMapper;
import com.team11.bookingservice.repository.BookingRepository;
import com.team11.jwtutil.AccessToken;
import com.team11.jwtutil.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final BookingEventPublisher bookingEventPublisher;
    private final JwtUtil jwtUtil;

    public BookingService(
        BookingRepository bookingRepository,
        BookingMapper bookingMapper,
        BookingEventPublisher bookingEventPublisher,
        Environment env
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.bookingEventPublisher = bookingEventPublisher;
        this.jwtUtil = new JwtUtil(new AccessToken(env));
    }

    @Transactional
    public Long extractUserId(String token) throws UnauthorizedException {
        if(jwtUtil.validateToken(token)) {
            log.debug("Token validation successful");
            return Long.parseLong(jwtUtil.extractUserIdString(token));
        } else {
            throw new UnauthorizedException("Invalid token");
        }
    }

    @Transactional
    public BookingEntity createBooking(BookingRequestModel requestModel, Long userId) throws BookingException {
        BookingEntity bookingEntity = bookingMapper.toEntity(requestModel, userId);
        log.debug("toEntity mapping successful");
        bookingRepository.save(bookingEntity);
        log.debug("bookingEntity saved successfully");
        BookingEventDTO dto = bookingMapper.toDTO(bookingEntity);
        log.debug("toDTO mapping successful");
        bookingEventPublisher.publish("FLIGHT", dto); // THIS IS HARDCODED FOR NOW
        log.debug("end of createBooking in BookingService");
        //TODO have to wait for getting reserved imp
        
        //TODO should throw exception
        return bookingEntity;
    }

    @Transactional
    public void handleResponse(BookingEventResponseDTO responseDTO) throws BookingException {
        Optional<BookingEntity> optionalBookingEntity = bookingRepository.findByBookingId(responseDTO.getBookingId());
        try {
            log.info("Was successful? : " + responseDTO.isSuccessful());
            BookingEntity bookingEntity = optionalBookingEntity.get();
            bookingEntity.setAmount(responseDTO.getAmount());
            bookingEntity.setStatus(responseDTO.isSuccessful() ? Status.RESERVED : Status.FAILED);
            // TODO somewhere in the service payment service needs to be called
            // if payment is successful then booking entity's status would change to confirmed.
            // this ends the entire booking flow.
            // TODO publish to notification service after booking flow is complete
            log.debug("end of handleResponse in BookingService");
        } catch (Exception e) {
            throw new BookingException(e, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Optional<BookingEntity> findByBookingId(UUID bookingId) {
        Optional<BookingEntity> optionalBookingEntity = bookingRepository.findByBookingId(bookingId);
        log.debug("end of findByBookingId in BookingService");
        return optionalBookingEntity;
    }

    public double getAmountByBookingId(String bookingId) throws BookingException {
        UUID uuid;
        try {
            uuid = UUID.fromString(bookingId);
        } catch (IllegalArgumentException e) {
            throw new BookingException("Invalid booking ID format: " + bookingId);
        }

        BookingEntity booking = bookingRepository.findByBookingId(uuid)
                .orElseThrow(() -> new BookingException("Booking not found for ID: " + bookingId));

        return booking.getAmount();
    }

    public Long getUserIdByBookingId(String bookingId) throws BookingException {
        UUID uuid;
        try {
            uuid = UUID.fromString(bookingId);
        } catch (IllegalArgumentException e) {
            throw new BookingException("Invalid booking ID format: " + bookingId);
        }

        BookingEntity booking = bookingRepository.findByBookingId(uuid)
                .orElseThrow(() -> new BookingException("Booking not found for ID: " + bookingId));

        return booking.getUserId();
    }



    public Status getBookingStatus(UUID bookingId) {
        // Simulate fetching booking from DB (You can replace this with actual logic)
        Optional<BookingEntity> booking = bookingRepository.findById(bookingId);

        return booking.map(BookingEntity::getStatus).orElse(null); // Return the status or null if not found
    }

    public void markBookingAsSuccessful(UUID bookingId) {
        Optional<BookingEntity> bookingOpt = bookingRepository.findByBookingId(bookingId);

        if (bookingOpt.isPresent()) {
            BookingEntity booking = bookingOpt.get();
            booking.setStatus(Status.CONFIRMED);
            bookingRepository.save(booking);
        } else {
            log.warn("Booking not found for ID: {}", bookingId);
        }
    }

    public List<BookingEntity> getSuccessfulBookingsByUserId(Long userId) {
        return bookingRepository.findByUserIdAndStatus(userId, Status.CONFIRMED);
    }

}
