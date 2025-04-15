package com.team11.bookingservice.entity;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.team11.bookingservice.enums.RequestType;
import com.team11.bookingservice.enums.Status;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "bookings",
    indexes = {
        @Index(name = "index_booking_id", columnList = "bookingId"),
        @Index(name = "index_user_id", columnList = "userId"),
        @Index(name = "index_status", columnList = "status"),
        @Index(name = "index_booking_created_at", columnList = "bookingCreatedAt"),
        @Index(name = "index_booking_modified_at", columnList = "bookingModifiedAt"),
    }
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private UUID bookingId;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING) // Stores the enum as string values in the db
    @Column(nullable = false)
    private Status status = Status.PENDING; // Default to PENDING

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType type;

    @Column(nullable = false)
    private double amount = 0.0;

    @ElementCollection
    @CollectionTable(
        name = "booking_details",
        joinColumns = @JoinColumn(name = "booking_id"),
        indexes = {
            @Index(name = "index_booking_details_key", columnList = "details_key")
        }
    )
    @MapKeyColumn(name = "details_key")
    @Column(name = "details_value")
    private Map<String, Long> details;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant bookingCreatedAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant bookingModifiedAt;

    public BookingEntity(){}

    public BookingEntity(Long userId, RequestType type, Map<String, Long> details) {
        this.userId = userId;
        this.type = type;
        this.details = new HashMap<>(details);
    }
}
