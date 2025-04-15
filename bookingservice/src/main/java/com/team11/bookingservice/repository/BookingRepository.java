package com.team11.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.team11.bookingservice.entity.BookingEntity;
import com.team11.bookingservice.enums.RequestType;
import com.team11.bookingservice.enums.Status;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<BookingEntity, UUID>, JpaSpecificationExecutor<BookingEntity> {

    Optional<BookingEntity> findByBookingId(UUID bookingId);

    List<BookingEntity> findByUserId(Long userId);

    List<BookingEntity> findByUserIdAndType(Long userId, RequestType type);

    List<BookingEntity> findByStatus(Status status);

    List<BookingEntity> findByBookingCreatedAt(Instant bookingCreatedAt);

    List<BookingEntity> findByBookingModifiedAt(Instant bookingModifiedAt);

    List<BookingEntity> findByUserIdAndStatus(Long userId, Status status);
}
