package com.team11.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, // Use the name of the subtype
    include = JsonTypeInfo.As.PROPERTY, // Include type info as a property in the JSON
    property = "requestType", // The property name to hold the type information
    visible = true // Make the type info available to Jackson
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = com.team11.bookingservice.dto.BookingEventDTO.class)
})
@Getter
@AllArgsConstructor
public class BookingEventDTO {
    // This bookingId acts as the correlation ID.
    // PUBLISHED OTHER SERVICE
    private final UUID bookingId;

    private final Map<String, Long> details;
}
