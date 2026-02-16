package travelagency.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TripReservationDto(
        String id,
        String customerName,
        int numberOfPeople,
        String status,
        LocalDate bookingDate,
        BigDecimal totalPrice
) {}
