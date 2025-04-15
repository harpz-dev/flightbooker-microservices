package com.team11.bookingservice;


import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import com.team11.bookingservice.dto.BookingEventDTO;
import com.team11.bookingservice.dto.BookingEventResponseDTO;
import com.team11.bookingservice.dto.BookingRequestModel;
import com.team11.bookingservice.entity.BookingEntity;
import com.team11.bookingservice.enums.Status;
import com.team11.bookingservice.exception.BookingException;
import com.team11.bookingservice.handler.event.publisher.BookingEventPublisher;
import com.team11.bookingservice.mapper.BookingMapper;
import com.team11.bookingservice.repository.BookingRepository;
import com.team11.bookingservice.service.BookingService;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingEventPublisher bookingEventPublisher;

    @Mock
    private Environment environment; // used to construct JwtUtil

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        // Initialize BookingService with mocks.
        // JwtUtil is constructed internally using the Environment.
        bookingService = new BookingService(bookingRepository, bookingMapper, bookingEventPublisher, environment);
    }

    @Test
    void testCreateBooking_success() throws BookingException {
        BookingRequestModel requestModel = new BookingRequestModel();
        Long userId = 100L;
        BookingEntity bookingEntity = new BookingEntity();
        UUID bookingId = UUID.randomUUID();
        when(bookingMapper.toEntity(requestModel, userId)).thenReturn(bookingEntity);
        BookingEventDTO eventDTO = new BookingEventDTO(bookingId, null);
        when(bookingMapper.toDTO(bookingEntity)).thenReturn(eventDTO);

        BookingEntity result = bookingService.createBooking(requestModel, userId);

        verify(bookingRepository).save(bookingEntity);
        verify(bookingEventPublisher).publish("FLIGHT", eventDTO);
        Assertions.assertEquals(bookingEntity, result);
    }

    @Test
    void testHandleResponse_success() throws BookingException {
        // Arrange: Create a real BookingEntity so we can check its field updates.
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setStatus(Status.FAILED); // initial status
        bookingEntity.setAmount(0.0);
        UUID bookingId = UUID.randomUUID();
        // (Assume we can associate bookingEntity with bookingId, e.g., via a setter or constructor)
        // For test, we assume bookingRepository.findByBookingId returns this bookingEntity.
        when(bookingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(bookingEntity));

        // Prepare a BookingEventResponseDTO.
        BookingEventResponseDTO responseDTO = new BookingEventResponseDTO(bookingId, true, 500);

        // Act: Call handleResponse
        bookingService.handleResponse(responseDTO);

        // Assert: The booking entity's amount should be updated and status set to RESERVED.
        Assertions.assertEquals(500.0, bookingEntity.getAmount());
        Assertions.assertEquals(Status.RESERVED, bookingEntity.getStatus());
    }

    @Test
    void testFindByBookingId() {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        BookingEntity bookingEntity = new BookingEntity();
        when(bookingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(bookingEntity));

        // Act
        Optional<BookingEntity> result = bookingService.findByBookingId(bookingId);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(bookingEntity, result.get());
    }

    @Test
    void testGetAmountByBookingId_success() throws BookingException {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        String bookingIdStr = bookingId.toString();
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setAmount(750.0);
        when(bookingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(bookingEntity));

        // Act
        double amount = bookingService.getAmountByBookingId(bookingIdStr);

        // Assert
        Assertions.assertEquals(750.0, amount);
    }

    @Test
    void testGetUserIdByBookingId_success() throws BookingException {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        String bookingIdStr = bookingId.toString();
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setUserId(200L);
        when(bookingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(bookingEntity));

        // Act
        Long userId = bookingService.getUserIdByBookingId(bookingIdStr);

        // Assert
        Assertions.assertEquals(200L, userId);
    }

    @Test
    void testGetBookingStatus() {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setStatus(Status.CONFIRMED);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));

        // Act
        Status status = bookingService.getBookingStatus(bookingId);

        // Assert
        Assertions.assertEquals(Status.CONFIRMED, status);
    }

    @Test
    void testMarkBookingAsSuccessful() {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setStatus(Status.FAILED);
        when(bookingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(bookingEntity));

        // Act
        bookingService.markBookingAsSuccessful(bookingId);

        // Assert: bookingEntity's status should be updated to CONFIRMED.
        Assertions.assertEquals(Status.CONFIRMED, bookingEntity.getStatus());
        verify(bookingRepository).save(bookingEntity);
    }

    @Test
    void testGetSuccessfulBookingsByUserId() {
        // Arrange
        Long userId = 300L;
        BookingEntity bookingEntity1 = new BookingEntity();
        bookingEntity1.setStatus(Status.CONFIRMED);
        BookingEntity bookingEntity2 = new BookingEntity();
        bookingEntity2.setStatus(Status.CONFIRMED);
        List<BookingEntity> confirmedBookings = List.of(bookingEntity1, bookingEntity2);
        when(bookingRepository.findByUserIdAndStatus(userId, Status.CONFIRMED))
            .thenReturn(confirmedBookings);

        // Act
        List<BookingEntity> result = bookingService.getSuccessfulBookingsByUserId(userId);

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(b -> b.getStatus() == Status.CONFIRMED));
    }
}
