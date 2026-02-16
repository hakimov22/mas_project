package travelagency.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationDto(
        String id,
        Long reservationId,
        Long customerId,
        String customerName,
        Long tripId,
        String tripName,
        int numberOfPeople,
        String status,
        LocalDate bookingDate,
        BigDecimal totalPrice
) {}
