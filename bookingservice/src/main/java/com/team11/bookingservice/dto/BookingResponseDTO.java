package com.team11.bookingservice.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingResponseDTO {
    // RESPONSE TO FRONTEND's INITIAL REQUEST
    // PROVIDES bookingId SO IT CAN BE QUERIED FROM THE FRONTEND WHEN NEEDED
    private final UUID bookingId;
}
