package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.BillingDto;
import com.dino.Mega_City_Cabs.entities.Billing;
import com.dino.Mega_City_Cabs.entities.Booking;
import com.dino.Mega_City_Cabs.entities.Customer;
import com.dino.Mega_City_Cabs.repositories.BillingRepository;
import com.dino.Mega_City_Cabs.repositories.BookingRepository;
import com.dino.Mega_City_Cabs.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public BillingDto createBilling(Long bookingId, String paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        if (booking.getStatus() != Booking.Status.COMPLETED) {
            throw new IllegalArgumentException("Billing can only be created for COMPLETED bookings");
        }
        if (booking.getBilling() != null) {
            throw new IllegalArgumentException("Billing already exists for this booking");
        }
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method is required");
        }
        if (!paymentMethod.matches("CASH|CREDIT_CARD")) {
            throw new IllegalArgumentException("Payment method must be CASH or CREDIT_CARD");
        }

        Customer customer = booking.getCustomer();
        BigDecimal totalAmount = BigDecimal.valueOf(booking.getTotalAmount());
        BigDecimal taxAmount = totalAmount.multiply(BigDecimal.valueOf(0.05)); // 5% tax
        BigDecimal discount = BigDecimal.ZERO; // Default, can be parameterized later

        Billing billing = new Billing();
        billing.setTotalAmount(totalAmount);
        billing.setTaxAmount(taxAmount);
        billing.setDiscount(discount);
        billing.setPaymentMethod(paymentMethod);
        billing.setBillingDate(Instant.now());
        billing.setCustomer(customer);
        billing.setBooking(booking);

        Billing savedBilling = billingRepository.save(billing);
        booking.setBilling(savedBilling);
        bookingRepository.save(booking);

        return convertToDto(savedBilling);
    }

    @Override
    public BillingDto getBillingById(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Billing not found: " + id));
        return convertToDto(billing);
    }

    private BillingDto convertToDto(Billing billing) {
        BillingDto dto = new BillingDto();
        dto.setId(billing.getId());
        dto.setTotalAmount(billing.getTotalAmount());
        dto.setTaxAmount(billing.getTaxAmount());
        dto.setDiscount(billing.getDiscount());
        dto.setPaymentMethod(billing.getPaymentMethod());
        dto.setCustomerId(billing.getCustomer().getId());
        dto.setBookingId(billing.getBooking().getId());
        return dto;
    }
}