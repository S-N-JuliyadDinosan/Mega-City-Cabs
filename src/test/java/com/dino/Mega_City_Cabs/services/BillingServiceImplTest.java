package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.BillingDto;
import com.dino.Mega_City_Cabs.entities.Billing;
import com.dino.Mega_City_Cabs.entities.Booking;
import com.dino.Mega_City_Cabs.entities.Customer;
import com.dino.Mega_City_Cabs.repositories.BillingRepository;
import com.dino.Mega_City_Cabs.repositories.BookingRepository;
import com.dino.Mega_City_Cabs.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceImplTest {

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private BillingServiceImpl billingService;

    private Booking booking;
    private Customer customer;
    private Billing billing;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        booking = new Booking();
        booking.setId(1L);
        booking.setCustomer(customer);
        booking.setTotalAmount(100.0);
        booking.setStatus(Booking.Status.COMPLETED);

        billing = new Billing();
        billing.setId(1L);
        billing.setTotalAmount(BigDecimal.valueOf(100.0));
        billing.setTaxAmount(BigDecimal.valueOf(5.0)); // 5% of 100
        billing.setDiscount(BigDecimal.ZERO);
        billing.setPaymentMethod("CASH");
        billing.setBillingDate(Instant.now());
        billing.setCustomer(customer);
        billing.setBooking(booking);
    }

    @Test
    void createBilling_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(billingRepository.save(any(Billing.class))).thenReturn(billing);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BillingDto result = billingService.createBilling(1L, "CASH");

        assertNotNull(result);
        assertEquals(billing.getTotalAmount(), result.getTotalAmount());
        assertEquals(billing.getTaxAmount(), result.getTaxAmount());
        assertEquals(billing.getPaymentMethod(), result.getPaymentMethod());
        assertEquals(booking.getId(), result.getBookingId());
        assertEquals(customer.getId(), result.getCustomerId());
        verify(billingRepository, times(1)).save(any(Billing.class));
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void createBilling_BookingNotFound_ThrowsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            billingService.createBilling(1L, "CASH");
        });

        assertEquals("Booking not found: 1", exception.getMessage());
        verify(billingRepository, never()).save(any(Billing.class));
    }

    @Test
    void createBilling_BookingNotCompleted_ThrowsException() {
        booking.setStatus(Booking.Status.IN_PROGRESS);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            billingService.createBilling(1L, "CASH");
        });

        assertEquals("Billing can only be created for COMPLETED bookings", exception.getMessage());
        verify(billingRepository, never()).save(any(Billing.class));
    }

    @Test
    void createBilling_BillingAlreadyExists_ThrowsException() {
        booking.setBilling(new Billing());
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            billingService.createBilling(1L, "CASH");
        });

        assertEquals("Billing already exists for this booking", exception.getMessage());
        verify(billingRepository, never()).save(any(Billing.class));
    }

    @Test
    void createBilling_InvalidPaymentMethod_ThrowsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            billingService.createBilling(1L, "INVALID");
        });

        assertEquals("Payment method must be CASH or CREDIT_CARD", exception.getMessage());
        verify(billingRepository, never()).save(any(Billing.class));
    }

    @Test
    void createBilling_NullPaymentMethod_ThrowsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            billingService.createBilling(1L, null);
        });

        assertEquals("Payment method is required", exception.getMessage());
        verify(billingRepository, never()).save(any(Billing.class));
    }

    @Test
    void getBillingById_Success() {
        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));

        BillingDto result = billingService.getBillingById(1L);

        assertNotNull(result);
        assertEquals(billing.getId(), result.getId());
        assertEquals(billing.getTotalAmount(), result.getTotalAmount());
        assertEquals(billing.getTaxAmount(), result.getTaxAmount());
        assertEquals(billing.getPaymentMethod(), result.getPaymentMethod());
        assertEquals(booking.getId(), result.getBookingId());
        assertEquals(customer.getId(), result.getCustomerId());
    }

    @Test
    void getBillingById_NotFound_ThrowsException() {
        when(billingRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            billingService.getBillingById(1L);
        });

        assertEquals("Billing not found: 1", exception.getMessage());
    }
}