package com.team11.bookingservice.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingEventResponseDTO {
    // CONSUMED FROM OTHER SERVICE AS A RESPONSE TO MY REQUEST
    private final UUID bookingId;
    @JsonProperty("successful")
    private final boolean isSuccessful;
    private final double amount;
    //price for the booking **

    @JsonCreator
    public BookingEventResponseDTO(
            @JsonProperty("bookingId") UUID bookingId,
            @JsonProperty("successful") boolean isSuccessful,
            @JsonProperty("amount") double amount
    ) {
        this.bookingId = bookingId;
        this.isSuccessful = isSuccessful;
        this.amount = amount;
    }
}
