package com.team11.bookingservice.dto;

import java.util.Map;

import com.team11.bookingservice.enums.RequestType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequestModel {
    // Frontend does not provide userId; it is extracted from the JWT.
    // Frontend only sends fields relevant for the specific booking type.
    // EXPECTED SCHEMA FROM FRONTEND --  GETS DEFINED IN API DOCS

    /**
{
  "requestType": "FLIGHT_BOOKING",
  "details": {
    "flightId": 40201,
    "numSeats": 2
  }
}
     */
    @NotNull
    private RequestType requestType;

    @NotNull
    private Map<String, Long> details;
}
