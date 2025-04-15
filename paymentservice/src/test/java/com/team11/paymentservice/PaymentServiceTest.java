package com.team11.paymentservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.team11.paymentservice.dto.PaymentRequestDTO;
import com.team11.paymentservice.entity.Payment;
import com.team11.paymentservice.enums.PaymentType;
import com.team11.paymentservice.enums.Status;
import com.team11.paymentservice.messaging.PaymentKafkaProducer;
import com.team11.paymentservice.repository.PaymentRepository;
import com.team11.paymentservice.service.PaymentService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentKafkaProducer paymentKafkaProducer;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentRequestDTO paymentRequestDTO;
    private Payment savedPayment;

    @BeforeEach
    void setUp() {
        paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setBookingId("ABC-123");
        paymentRequestDTO.setMethod(PaymentType.CREDIT_CARD);

        savedPayment = new Payment();
        // savedPayment.setId(1L); // ID is auto-generated
        savedPayment.setBookingId("ABC-123");
        savedPayment.setPaymentStatus(Status.PENDING);

        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
    }

    @Test
    void testProcessPayment_Success() {
        Payment result = paymentService.processPayment(paymentRequestDTO);

        assertNotNull(result);
        // Uncomment the following line if your service or test manually sets an ID.
        // assertNotNull(result.getId(), "ID should be auto-generated and not null");
        assertEquals("ABC-123", result.getBookingId());
        assertEquals(Status.PENDING, result.getPaymentStatus());

        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(paymentKafkaProducer, times(1)).requestAmount(any());
    }

    @Test
    void testGetPaymentDetails_Found() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(savedPayment));
        Optional<Payment> paymentOpt = paymentService.getPaymentDetails(1L);
        assertTrue(paymentOpt.isPresent());
        assertEquals("ABC-123", paymentOpt.get().getBookingId());
        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPaymentDetails_NotFound() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Payment> paymentOpt = paymentService.getPaymentDetails(999L);
        assertFalse(paymentOpt.isPresent());
        verify(paymentRepository, times(1)).findById(999L);
    }
}
