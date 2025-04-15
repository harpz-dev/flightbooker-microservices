package com.team11.bookingservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that handles requests to the home endpoint.
 */
@RestController
@RequestMapping("/")
public class HomeController {

    /**
     * Handles GET requests for the root URL.
     *
     * @return a welcome message.
     */
    @GetMapping
    public String home() {
        return "Welcome to the Booking Service!";
    }
}
