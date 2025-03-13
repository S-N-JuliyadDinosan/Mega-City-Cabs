package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.BookingDto;
import com.dino.Mega_City_Cabs.dtos.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingDto bookingDto);
    BookingResponseDto getBookingById(Long id);
    List<BookingResponseDto> getPendingBookings();
    List<BookingResponseDto> getCustomerBookings(Long customerId);
    List<BookingResponseDto> getDriverBookings(Long driverId);
    BookingResponseDto assignDriver(Long bookingId, Long driverId, Long carId);
    BookingResponseDto confirmBooking(Long bookingId);
    BookingResponseDto cancelBooking(Long bookingId);
    BookingResponseDto completeBooking(Long bookingId);
    double calculateTotalAmount(double distanceKm); // New method
}