package travelagency.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CustomerReservationDto(
        String id,
        int numberOfPeople,
        String status,
        LocalDate bookingDate,
        BigDecimal totalPrice,
        Long tripId,
        String tripName,
        String tripType,
        String destination,
        String country,
        LocalDate departureDate,
        LocalDate returnDate,
        int duration,
        BigDecimal pricePerPerson
) {}
