package travelagency.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TripDto(
        long id,
        String tripCode,
        String name,
        String type,
        String description,
        String destination,
        String country,
        String climate,
        LocalDate departureDate,
        LocalDate returnDate,
        int duration,
        BigDecimal basePrice,
        BigDecimal finalPrice,
        int maxParticipants,
        int availableSpots,
        String difficultyLevel,
        Boolean equipmentIncluded,
        Boolean guidedTours,
        List<String> historicalSites,
        String resortName,
        Boolean allInclusive
) {}
