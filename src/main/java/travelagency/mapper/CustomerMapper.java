package travelagency.mapper;

import travelagency.dto.CustomerDto;
import travelagency.dto.CustomerReservationDto;
import travelagency.model.Customer;
import travelagency.model.Reservation;
import travelagency.model.Trip;

public final class CustomerMapper {
    private CustomerMapper() {}

    public static CustomerDto toDto(Customer c) {
        return new CustomerDto(
                c.getCustomerId(),
                c.getFullName(),
                c.getEmail(),
                c.getRegistrationDate()
        );
    }

    public static CustomerReservationDto toReservationDto(Reservation r) {
        Trip t = r.getTrip();
        return new CustomerReservationDto(
                r.getReservationNumber(),
                r.getNumberOfPeople(),
                r.getStatus().name(),
                r.getBookingDate(),
                r.getTotalPrice(),
                t != null ? t.getTripId() : null,
                t != null ? t.getName() : null,
                t != null ? t.getTripType() : null,
                t != null && t.getDestination() != null ? t.getDestination().getName() : "",
                t != null && t.getDestination() != null ? t.getDestination().getCountry() : "",
                t != null ? t.getDepartureDate() : null,
                t != null ? t.getReturnDate() : null,
                t != null ? t.getDuration() : 0,
                t != null ? t.getFinalPrice() : null
        );
    }
}
