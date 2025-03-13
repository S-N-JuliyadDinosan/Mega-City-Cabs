package com.dino.Mega_City_Cabs.controllers;

import com.dino.Mega_City_Cabs.dtos.BookingDto;
import com.dino.Mega_City_Cabs.dtos.BookingResponseDto;
import com.dino.Mega_City_Cabs.enums.RestApiResponseStatusCodes;
import com.dino.Mega_City_Cabs.services.BookingService;
import com.dino.Mega_City_Cabs.utils.EndpointBundle;
import com.dino.Mega_City_Cabs.utils.ResponseWrapper;
import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.BOOKING)
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Customer: Create Booking
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ResponseWrapper<BookingResponseDto>> createBooking(
            @Valid @RequestBody BookingDto bookingDto) {
        try {
            BookingResponseDto booking = bookingService.createBooking(bookingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CREATED.getCode(),
                    ValidationMessages.SAVED_SUCCESSFULL,
                    booking
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.SAVE_FAILED,
                    null
            ));
        }
    }

    // Customer/Admin: Get Booking by ID
    @GetMapping(EndpointBundle.ID)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<BookingResponseDto>> getBookingById(@PathVariable Long id) {
        try {
            BookingResponseDto booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    booking
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }

    // Admin: Get Pending Bookings
    @GetMapping(EndpointBundle.PENDING_BOOKINGS)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<List<BookingResponseDto>>> getPendingBookings() {
        try {
            List<BookingResponseDto> bookings = bookingService.getPendingBookings();
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    bookings
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }

    // Customer: Get Their Bookings
    @GetMapping(EndpointBundle.CUSTOMER_GET_THEIR_BOOKINGS)
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ResponseWrapper<List<BookingResponseDto>>> getCustomerBookings(
            @PathVariable Long customerId) {
        try {
            List<BookingResponseDto> bookings = bookingService.getCustomerBookings(customerId);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    bookings
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }

    // Driver: Get Their Bookings
    @GetMapping(EndpointBundle.DRIVER_GET_THEIR_BOOKINGS)
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<ResponseWrapper<List<BookingResponseDto>>> getDriverBookings(
            @PathVariable Long driverId) {
        try {
            List<BookingResponseDto> bookings = bookingService.getDriverBookings(driverId);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    bookings
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }

    // Admin: Assign Driver
    @PutMapping(EndpointBundle.ADMIN_ASSIGN_DRIVER)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<BookingResponseDto>> assignDriver(
            @PathVariable Long id, @RequestParam Long driverId, @RequestParam Long carId) {
        try {
            BookingResponseDto booking = bookingService.assignDriver(id, driverId, carId);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    "Driver assigned successfully",
                    booking
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.UPDATE_FAILED,
                    null
            ));
        }
    }

    // Customer: Confirm Booking (after seeing amount)
    @PutMapping(EndpointBundle.CUSTOMER_CONFIRM_BOOKING_BY_AMOUNT)
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ResponseWrapper<BookingResponseDto>> confirmBooking(@PathVariable Long id) {
        try {
            BookingResponseDto booking = bookingService.confirmBooking(id);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    "Booking confirmed",
                    booking
            ));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.FORBIDDEN.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.UPDATE_FAILED,
                    null
            ));
        }
    }

    // Customer/Admin: Cancel Booking
    @PutMapping(EndpointBundle.CUSTOMER_OR_ADMIN_CANCEL_BOOKING)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<BookingResponseDto>> cancelBooking(@PathVariable Long id) {
        try {
            BookingResponseDto booking = bookingService.cancelBooking(id);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    "Booking cancelled",
                    booking
            ));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.FORBIDDEN.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.UPDATE_FAILED,
                    null
            ));
        }
    }

    // Driver: Complete Booking
    @PutMapping(EndpointBundle.DRIVER_COMPLETE_BOOKING)
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<ResponseWrapper<BookingResponseDto>> completeBooking(@PathVariable Long id) {
        try {
            BookingResponseDto booking = bookingService.completeBooking(id);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    "Booking completed",
                    booking
            ));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.FORBIDDEN.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.UPDATE_FAILED,
                    null
            ));
        }
    }
}