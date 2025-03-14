package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.BillingDto;
import com.dino.Mega_City_Cabs.dtos.BookingDto;
import com.dino.Mega_City_Cabs.dtos.BookingResponseDto;
import com.dino.Mega_City_Cabs.dtos.SystemLogDto;
import com.dino.Mega_City_Cabs.entities.*;
import com.dino.Mega_City_Cabs.enums.AvailabilityStatus;
import com.dino.Mega_City_Cabs.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private PricingService pricingService;

    @Mock
    private BillingService billingService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private SystemLogService systemLogService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Customer customer;
    private User customerUser;
    private Driver driver;
    private User driverUser;
    private Car car;
    private Booking booking;
    private BookingDto bookingDto;
    private BillingDto billingDto;

    @BeforeEach
    void setUp() {
        customerUser = new User();
        customerUser.setId(1L);
        customerUser.setEmail("customer@example.com");

        customer = new Customer();
        customer.setId(1L);
        customer.setUser(customerUser);

        driverUser = new User();
        driverUser.setId(2L);
        driverUser.setEmail("driver@example.com");

        driver = new Driver();
        driver.setId(1L);
        driver.setUser(driverUser);
        driver.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        car = new Car();
        car.setId(1L);

        booking = new Booking();
        booking.setId(1L);
        booking.setCustomer(customer);
        booking.setPickUpLocation("Location A");
        booking.setDestinationDetails("Location B");
       // booking.setBookingDateTime(Instant.now());
        booking.setStatus(Booking.Status.PENDING);
        booking.setDistanceKm(10.0);
        booking.setTotalAmount(100.0);

        bookingDto = new BookingDto();
        bookingDto.setCustomerId(1L);
        bookingDto.setPickUpLocation("Location A");
        bookingDto.setDestinationDetails("Location B");
        //bookingDto.setBookingDateTime(Instant.now());
        bookingDto.setDistanceKm(10.0);

        billingDto = new BillingDto();
        billingDto.setId(1L);
        //billingDto.setTotalAmount(100.0);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void createBooking_Success() {
        when(authentication.getName()).thenReturn("customer@example.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(pricingService.calculateTotalAmount(10.0)).thenReturn(100.0);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDto result = bookingService.createBooking(bookingDto);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStatus().name(), result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(systemLogService, times(1)).logAction(any(SystemLogDto.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/bookings"), any(BookingResponseDto.class));
    }

    @Test
    void confirmBooking_Success() {
        when(authentication.getName()).thenReturn("customer@example.com");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingResponseDto result = bookingService.confirmBooking(1L);

        assertEquals(Booking.Status.CONFIRMED.name(), result.getStatus());
        verify(bookingRepository, times(1)).save(booking);
    }





    @Test
    void startRide_Success() {
        booking.setStatus(Booking.Status.ASSIGNED);
        booking.setDriver(driver);
        when(authentication.getName()).thenReturn("driver@example.com");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingResponseDto result = bookingService.startRide(1L);

        assertEquals(Booking.Status.IN_PROGRESS.name(), result.getStatus());
        verify(bookingRepository, times(1)).save(booking);
    }


    @Test
    void completeBooking_Success() {
        booking.setStatus(Booking.Status.IN_PROGRESS);
        booking.setDriver(driver);
        when(authentication.getName()).thenReturn("driver@example.com");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(driverRepository.save(driver)).thenReturn(driver);
        when(billingService.createBilling(1L, "CASH")).thenReturn(billingDto);

        BookingResponseDto result = bookingService.completeBooking(1L);

        assertEquals(Booking.Status.COMPLETED.name(), result.getStatus());
        verify(billingService, times(1)).createBilling(1L, "CASH");
        verify(messagingTemplate, times(2)).convertAndSend(anyString(), eq(billingDto));
    }



    @Test
    void cancelBooking_Success() {
        when(authentication.getName()).thenReturn("customer@example.com");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingResponseDto result = bookingService.cancelBooking(1L);

        assertEquals(Booking.Status.CANCELLED.name(), result.getStatus());
        verify(bookingRepository, times(1)).save(booking);
    }



}