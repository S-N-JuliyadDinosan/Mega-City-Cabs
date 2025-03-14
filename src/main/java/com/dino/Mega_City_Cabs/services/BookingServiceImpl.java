package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.BillingDto;
import com.dino.Mega_City_Cabs.dtos.BookingDto;
import com.dino.Mega_City_Cabs.dtos.BookingResponseDto;
import com.dino.Mega_City_Cabs.dtos.SystemLogDto;
import com.dino.Mega_City_Cabs.entities.*;
import com.dino.Mega_City_Cabs.enums.AvailabilityStatus;
import com.dino.Mega_City_Cabs.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PricingService pricingService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SystemLogService systemLogService;

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingDto bookingDto) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Customer customer = customerRepository.findById(bookingDto.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + bookingDto.getCustomerId()));
            if (!customer.getUser().getEmail().equals(currentUserEmail)) {
                throw new SecurityException("Unauthorized to create booking for this customer");
            }

            Booking booking = new Booking();
            booking.setPickUpLocation(bookingDto.getPickUpLocation());
            booking.setDestinationDetails(bookingDto.getDestinationDetails());
            booking.setBookingDateTime(bookingDto.getBookingDateTime());
            booking.setStatus(Booking.Status.PENDING);
            booking.setCustomer(customer);
            booking.setDistanceKm(bookingDto.getDistanceKm());
            booking.setTotalAmount(calculateTotalAmount(bookingDto.getDistanceKm()));

            Booking savedBooking = bookingRepository.save(booking);

            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("BOOKING_CREATED");
            log.setCustomerId(customer.getId());
            log.setLogLevel("INFO");
            systemLogService.logAction(log);

            BookingResponseDto responseDto = convertToResponseDto(savedBooking);
            messagingTemplate.convertAndSend("/topic/bookings", responseDto);

            return responseDto;
        } catch (Exception e) {
            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("BOOKING_CREATION_ERROR");
            log.setCustomerId(bookingDto.getCustomerId());
            log.setLogLevel("ERROR");
            systemLogService.logAction(log);
            throw new RuntimeException("Failed to create booking: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public BookingResponseDto confirmBooking(Long bookingId) {
        // Unchanged from previous version
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
            if (!booking.getCustomer().getUser().getEmail().equals(currentUserEmail)) {
                throw new SecurityException("Unauthorized to confirm this booking");
            }
            if (booking.getStatus() != Booking.Status.PENDING) {
                throw new IllegalArgumentException("Can only confirm PENDING bookings");
            }

            booking.setStatus(Booking.Status.CONFIRMED);
            Booking updatedBooking = bookingRepository.save(booking);

            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("BOOKING_CONFIRMED");
            log.setCustomerId(booking.getCustomer().getId());
            log.setLogLevel("INFO");
            systemLogService.logAction(log);

            return convertToResponseDto(updatedBooking);
        } catch (Exception e) {
            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("BOOKING_CONFIRMATION_FAILED");
            log.setCustomerId(getCustomerIdForBooking(bookingId));
            log.setLogLevel("ERROR");
            systemLogService.logAction(log);
            throw new RuntimeException("Failed to confirm booking: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public BookingResponseDto assignDriver(Long bookingId, Long driverId, Long carId) {
        // Unchanged from previous version
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
            if (booking.getStatus() != Booking.Status.CONFIRMED) {
                throw new IllegalArgumentException("Can only assign driver to CONFIRMED bookings");
            }

            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));
            if (driver.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE) {
                throw new IllegalArgumentException("Driver is not available: " + driverId);
            }

            Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));

            booking.setDriver(driver);
            booking.setCar(car);
            booking.setStatus(Booking.Status.ASSIGNED);
            driver.setAvailabilityStatus(AvailabilityStatus.ON_DUTY);
            driverRepository.save(driver);

            Booking updatedBooking = bookingRepository.save(booking);

            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("DRIVER_ASSIGNED");
            log.setCustomerId(booking.getCustomer().getId());
            log.setDriverId(driverId);
            log.setLogLevel("INFO");
            systemLogService.logAction(log);

            return convertToResponseDto(updatedBooking);
        } catch (Exception e) {
            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("DRIVER_ASSIGNMENT_FAILED");
            log.setCustomerId(getCustomerIdForBooking(bookingId));
            log.setDriverId(driverId);
            log.setLogLevel("ERROR");
            systemLogService.logAction(log);
            throw new RuntimeException("Failed to assign driver: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public BookingResponseDto startRide(Long bookingId) {
        // Unchanged from previous version
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
            if (booking.getDriver() == null || !booking.getDriver().getUser().getEmail().equals(currentUserEmail)) {
                throw new SecurityException("Unauthorized to start this ride");
            }
            if (booking.getStatus() != Booking.Status.ASSIGNED) {
                throw new IllegalArgumentException("Can only start ASSIGNED bookings");
            }

            booking.setStatus(Booking.Status.IN_PROGRESS);
            Booking updatedBooking = bookingRepository.save(booking);

            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("RIDE_STARTED");
            log.setCustomerId(booking.getCustomer().getId());
            log.setDriverId(booking.getDriver().getId());
            log.setLogLevel("INFO");
            systemLogService.logAction(log);

            return convertToResponseDto(updatedBooking);
        } catch (Exception e) {
            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("RIDE_START_FAILED");
            log.setCustomerId(getCustomerIdForBooking(bookingId));
            log.setDriverId(getDriverIdForBooking(bookingId));
            log.setLogLevel("ERROR");
            systemLogService.logAction(log);
            throw new RuntimeException("Failed to start ride: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public BookingResponseDto completeBooking(Long bookingId) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
            if (booking.getDriver() == null || !booking.getDriver().getUser().getEmail().equals(currentUserEmail)) {
                throw new SecurityException("Unauthorized to complete this booking");
            }
            if (booking.getStatus() != Booking.Status.IN_PROGRESS) {
                throw new IllegalArgumentException("Can only complete IN_PROGRESS bookings");
            }

            booking.setStatus(Booking.Status.COMPLETED);
            booking.getDriver().setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            driverRepository.save(booking.getDriver());
            Booking updatedBooking = bookingRepository.save(booking);

            // Create billing after completion (default to CASH, can be parameterized later)
            BillingDto billingDto = billingService.createBilling(bookingId, "CASH");

            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("BOOKING_COMPLETED");
            log.setCustomerId(booking.getCustomer().getId());
            log.setDriverId(booking.getDriver().getId());
            log.setLogLevel("INFO");
            systemLogService.logAction(log);

            // Notify customer and driver about billing
            messagingTemplate.convertAndSend("/topic/billing/customer/" + booking.getCustomer().getId(), billingDto);
            messagingTemplate.convertAndSend("/topic/billing/driver/" + booking.getDriver().getId(), billingDto);

            return convertToResponseDto(updatedBooking);
        } catch (Exception e) {
            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("BOOKING_COMPLETION_FAILED");
            log.setCustomerId(getCustomerIdForBooking(bookingId));
            log.setDriverId(getDriverIdForBooking(bookingId));
            log.setLogLevel("ERROR");
            systemLogService.logAction(log);
            throw new RuntimeException("Failed to complete booking: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public BookingResponseDto cancelBooking(Long bookingId) {
        // Unchanged from previous version
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
            boolean isCustomer = booking.getCustomer().getUser().getEmail().equals(currentUserEmail);
            boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                    .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isCustomer && !isAdmin) {
                throw new SecurityException("Unauthorized to cancel this booking");
            }
            if (booking.getStatus() != Booking.Status.PENDING && booking.getStatus() != Booking.Status.CONFIRMED) {
                throw new IllegalArgumentException("Can only cancel PENDING or CONFIRMED bookings");
            }

            booking.setStatus(Booking.Status.CANCELLED);
            Booking updatedBooking = bookingRepository.save(booking);

            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("BOOKING_CANCELLED");
            log.setCustomerId(booking.getCustomer().getId());
            log.setLogLevel("INFO");
            systemLogService.logAction(log);

            return convertToResponseDto(updatedBooking);
        } catch (Exception e) {
            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("BOOKING_CANCELLATION_FAILED");
            log.setCustomerId(getCustomerIdForBooking(bookingId));
            log.setLogLevel("ERROR");
            systemLogService.logAction(log);
            throw new RuntimeException("Failed to cancel booking: " + e.getMessage(), e);
        }
    }

    @Override
    public BookingResponseDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
        return convertToResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getPendingBookings() {
        return bookingRepository.findByStatus(Booking.Status.PENDING)
                .stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getCustomerBookings(Long customerId) {
        return bookingRepository.findByCustomerId(customerId)
                .stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getDriverBookings(Long driverId) {
        return bookingRepository.findByDriverId(driverId)
                .stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    @Override
    public double calculateTotalAmount(double distanceKm) {
        return pricingService.calculateTotalAmount(distanceKm);
    }

    private BookingResponseDto convertToResponseDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setPickUpLocation(booking.getPickUpLocation());
        dto.setDestinationDetails(booking.getDestinationDetails());
        dto.setBookingDateTime(booking.getBookingDateTime());
        dto.setStatus(booking.getStatus().name());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setDriverId(booking.getDriver() != null ? booking.getDriver().getId() : null);
        dto.setCarId(booking.getCar() != null ? booking.getCar().getId() : null);
        dto.setBillingId(booking.getBilling() != null ? booking.getBilling().getId() : null);
        dto.setDistanceKm(booking.getDistanceKm());
        dto.setTotalAmount(booking.getTotalAmount());
        return dto;
    }

    private Long getCustomerIdForBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getCustomer().getId())
                .orElse(null);
    }

    private Long getDriverIdForBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getDriver() != null ? booking.getDriver().getId() : null)
                .orElse(null);
    }
}