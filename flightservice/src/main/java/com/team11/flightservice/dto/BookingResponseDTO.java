package com.team11.flightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class BookingResponseDTO {
    private final UUID bookingId;
    private final boolean isSuccessful;
    private final double amount;
}
