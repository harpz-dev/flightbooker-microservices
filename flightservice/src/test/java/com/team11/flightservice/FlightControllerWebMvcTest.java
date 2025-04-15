package com.team11.flightservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.team11.flightservice.controller.FlightController;
import com.team11.flightservice.entity.Flight;
import com.team11.flightservice.service.FlightService;

@WebMvcTest(controllers = FlightController.class)
class FlightControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Test
    void testGetAvailableFlights() throws Exception {
        Flight mockFlight = Mockito.mock(Flight.class);
        Mockito.when(mockFlight.getId()).thenReturn(123L);
        Mockito.when(mockFlight.getPrice()).thenReturn(99.99);

        LocalDate date = LocalDate.of(2025, 2, 1);
        Mockito.when(flightService.getAvailableFlights("Paris", "Berlin", date))
            .thenReturn(List.of(mockFlight));

        mockMvc.perform(get("/api/flights/search")
                .param("source", "Paris")
                .param("destination", "Berlin")
                .param("departureDate", "2025-02-01"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(123L))
            .andExpect(jsonPath("$[0].price").value(99.99));
    }

}
