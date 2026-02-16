package travelagency.mapper;

import travelagency.dto.ReservationDto;
import travelagency.model.Reservation;

public final class ReservationMapper {
    private ReservationMapper() {}

    public static ReservationDto toDto(Reservation r) {
        Long customerId = r.getCustomer() != null ? r.getCustomer().getCustomerId() : null;
        String customerName = r.getCustomer() != null ? r.getCustomer().getFullName() : "Unknown";
        Long tripId = r.getTrip() != null ? r.getTrip().getTripId() : null;
        String tripName = r.getTrip() != null ? r.getTrip().getName() : "Unknown";

        return new ReservationDto(
                r.getReservationNumber(),
                r.getReservationId(),
                customerId,
                customerName,
                tripId,
                tripName,
                r.getNumberOfPeople(),
                r.getStatus().name(),
                r.getBookingDate(),
                r.getTotalPrice()
        );
    }
}
