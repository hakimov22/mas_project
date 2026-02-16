package travelagency.dto;

import java.time.LocalDate;

public record CustomerDto(
        long id,
        String name,
        String email,
        LocalDate registrationDate
) {}
