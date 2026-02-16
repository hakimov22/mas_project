package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import travelagency.model.enums.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Trip class with disjoint inheritance.
 * 
 * Disjoint Constraint: A Trip instance can be EXACTLY ONE of:
 * - AdventureTrip (mutually exclusive)
 * - CulturalTrip (mutually exclusive)
 * - VacationTrip (mutually exclusive)
 * 
 * No Trip can be multiple types simultaneously (disjoint specialization).
 */
@Entity
@Table(name = "trips")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "trip_type")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"destination", "reservations"})
public abstract class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    protected Long tripId;
    @Column(unique = true)
    protected String tripCode;
    protected String name;
    protected String description;
    protected LocalDate departureDate;
    protected LocalDate returnDate;
    protected BigDecimal basePrice;
    protected int maxParticipants;
    @ManyToOne
    @JoinColumn(name = "destination_id")
    protected Destination destination;
    @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    protected Flight flight;
    @OneToMany(mappedBy = "trip", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    protected List<Reservation> reservations = new ArrayList<>();

    // ===== Constructor Overloading =====
    public Trip(String code, String name, String desc, Destination dest,
            LocalDate dep, LocalDate ret, BigDecimal price, int max) {
        this.tripCode = code;
        this.name = name;
        this.description = desc;
        this.departureDate = dep;
        this.returnDate = ret;
        this.basePrice = price;
        this.maxParticipants = max;
        setDestination(dest);
    }

    // ===== Abstract (Polymorphism) =====
    public abstract BigDecimal getFinalPrice();
    public abstract String getTripType();

    // ===== Business Methods =====
    public int getDuration() {
        return (int) ChronoUnit.DAYS.between(departureDate, returnDate);
    }

    // ===== Method Overloading =====
    // Overloaded method: check availability (default: current date)
    public boolean isAvailable() {
        return isAvailable(LocalDate.now());
    }

    // Overloaded method: check availability at specific date
    public boolean isAvailable(LocalDate checkDate) {
        return departureDate.isAfter(checkDate) && getAvailableSpots() > 0;
    }

    public int getAvailableSpots() {
        int booked = reservations.stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .mapToInt(Reservation::getNumberOfPeople).sum();
        return maxParticipants - booked;
    }

    public boolean hasEnoughSpots(int people) {
        return getAvailableSpots() >= people;
    }

    // ===== Association: Trip <-> Destination =====
    public void setDestination(Destination destination) {
        if (this.destination != destination) {
            if (this.destination != null) this.destination.removeTrip(this);
            this.destination = destination;
            if (destination != null) destination.addTrip(this);
        }
    }

    // ===== Association: Trip <-> Reservation =====
    public void addReservation(Reservation r) {
        if (!reservations.contains(r)) {
            reservations.add(r);
            if (r.getTrip() != this) r.setTrip(this);
        }
    }

    public void removeReservation(Reservation r) {
        if (reservations.remove(r) && r.getTrip() == this) r.setTrip(null);
    }

}
