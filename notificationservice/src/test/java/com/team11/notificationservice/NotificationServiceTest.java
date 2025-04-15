package com.team11.notificationservice;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team11.notificationservice.dto.PaymentEventDTO;
import com.team11.notificationservice.service.NotificationService;
import com.team11.notificationservice.service.UserServiceClient;
import com.team11.notificationservice.service.strategy.NotificationStrategy;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private PaymentEventDTO paymentEventDTOService;

    @Mock
    private NotificationStrategy notificationStrategy;

    @InjectMocks
    private NotificationService notificationService;

    private PaymentEventDTO event;

    @BeforeEach
    void setUp() {
        event = new PaymentEventDTO();
        event.setUserId(123L);
        event.setBookingId("ABC-123");
        event.setStatus("CONFIRMED");
    }

    @Test
    void testHandleNotification_Success() {
        when(userServiceClient.getUserById(123L)).thenReturn("user@example.com");
        notificationService.handleNotification(event);
        verify(notificationStrategy).send("user@example.com", "Your booking with ID ABC-123 is CONFIRMED.");
    }

    @Test
    void testHandleNotification_NoEmailFound() {
        when(userServiceClient.getUserById(123L)).thenReturn("");
        notificationService.handleNotification(event);
        verify(notificationStrategy, never()).send(anyString(), anyString());
    }
}