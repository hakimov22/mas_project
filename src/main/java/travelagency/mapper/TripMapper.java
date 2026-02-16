package travelagency.mapper;

import travelagency.dto.TripDto;
import travelagency.dto.TripReservationDto;
import travelagency.model.AdventureTrip;
import travelagency.model.CulturalTrip;
import travelagency.model.Reservation;
import travelagency.model.Trip;
import travelagency.model.VacationTrip;

import java.util.List;

public final class TripMapper {
    private TripMapper() {}

    public static TripDto toDto(Trip t) {
        String destinationName = t.getDestination() != null ? t.getDestination().getName() : "";
        String destinationCountry = t.getDestination() != null ? t.getDestination().getCountry() : "";
        String destinationClimate = t.getDestination() != null ? t.getDestination().getClimate() : "";

        String difficultyLevel = null;
        Boolean equipmentIncluded = null;
        Boolean guidedTours = null;
        List<String> historicalSites = null;
        String resortName = null;
        Boolean allInclusive = null;

        if (t instanceof AdventureTrip at) {
            difficultyLevel = at.getDifficultyLevel().name();
            equipmentIncluded = at.isEquipmentIncluded();
        } else if (t instanceof CulturalTrip ct) {
            guidedTours = ct.isGuidedTours();
            historicalSites = ct.getHistoricalSites();
        } else if (t instanceof VacationTrip vt) {
            resortName = vt.getResortName();
            allInclusive = vt.isAllInclusive();
        }

        return new TripDto(
                t.getTripId(),
                t.getTripCode(),
                t.getName(),
                t.getTripType(),
                t.getDescription(),
                destinationName,
                destinationCountry,
                destinationClimate,
                t.getDepartureDate(),
                t.getReturnDate(),
                t.getDuration(),
                t.getBasePrice(),
                t.getFinalPrice(),
                t.getMaxParticipants(),
                t.getAvailableSpots(),
                difficultyLevel,
                equipmentIncluded,
                guidedTours,
                historicalSites,
                resortName,
                allInclusive
        );
    }

    public static TripReservationDto toReservationDto(Reservation r) {
        String customerName = r.getCustomer() != null ? r.getCustomer().getFullName() : "Unknown";
        return new TripReservationDto(
                r.getReservationNumber(),
                customerName,
                r.getNumberOfPeople(),
                r.getStatus().name(),
                r.getBookingDate(),
                r.getTotalPrice()
        );
    }
}
