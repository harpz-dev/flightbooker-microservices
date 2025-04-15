package com.team11.flightservice.dto;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingRequestDTO {
    private final UUID bookingId;
    private final Map<String, Long> details;

    @JsonCreator
    public BookingRequestDTO(
        @JsonProperty("bookingId") UUID bookingId,
        @JsonProperty("details") Map<String, Long> details
    ) {
        this.bookingId = bookingId;
        this.details = details;
    }
}