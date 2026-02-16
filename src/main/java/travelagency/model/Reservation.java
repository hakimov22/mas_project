package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import travelagency.model.enums.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "reservations")
@Getter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"customer", "trip", "payment"})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter private Long reservationId;
    @EqualsAndHashCode.Include
    @Column(unique = true, nullable = false)
    private String reservationNumber;
    private LocalDate bookingDate;
    @Enumerated(EnumType.STRING)
    @Setter private ReservationStatus status;
    private int numberOfPeople;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;
    @OneToOne(mappedBy = "reservation", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Payment payment;

    public Reservation(Customer customer, Trip trip, int numberOfPeople) {
        this.reservationNumber = generateNumber();
        this.bookingDate = LocalDate.now();
        this.status = ReservationStatus.PENDING;
        setNumberOfPeople(numberOfPeople);
        setCustomer(customer);
        setTrip(trip);
    }

    public void setNumberOfPeople(int numberOfPeople) {
        if (numberOfPeople < 1) {
            throw new IllegalArgumentException("Number of people must be at least 1");
        }
        this.numberOfPeople = numberOfPeople;
    }

    // ===== Derived Attribute =====
    public BigDecimal getTotalPrice() {
        return trip == null ? BigDecimal.ZERO
                : trip.getFinalPrice().multiply(BigDecimal.valueOf(numberOfPeople));
    }

    public boolean canRefund() {
        return trip != null && ChronoUnit.DAYS.between(LocalDate.now(), trip.getDepartureDate()) >= 10;
    }

    // ===== State Transitions =====
    public void confirm() {
        if (status != ReservationStatus.PENDING)
            throw new IllegalStateException("Can only confirm PENDING");
        this.status = ReservationStatus.CONFIRMED;
    }

    public void recordPayment(Payment payment) {
        if (status != ReservationStatus.PENDING)
            throw new IllegalStateException("Can only pay PENDING");
        if (!payment.isValid() || !payment.getAmount().equals(getTotalPrice()))
            throw new IllegalArgumentException("Invalid payment");
        this.payment = payment;
        payment.setReservation(this);
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == ReservationStatus.COMPLETED || status == ReservationStatus.CANCELLED)
            throw new IllegalStateException("Cannot cancel");
        this.status = ReservationStatus.CANCELLED;
    }

    public void complete() {
        if (status != ReservationStatus.CONFIRMED)
            throw new IllegalStateException("Can only complete CONFIRMED");
        if (trip.getDepartureDate().isAfter(LocalDate.now()))
            throw new IllegalStateException("Trip not departed");
        this.status = ReservationStatus.COMPLETED;
    }

    // ===== Association: Reservation <-> Customer =====
    public void setCustomer(Customer customer) {
        if (this.customer != customer) {
            if (this.customer != null) this.customer.removeReservation(this);
            this.customer = customer;
            if (customer != null) customer.addReservation(this);
        }
    }

    // ===== Association: Reservation <-> Trip =====
    public void setTrip(Trip trip) {
        if (this.trip != trip) {
            if (this.trip != null) this.trip.removeReservation(this);
            this.trip = trip;
            if (trip != null) trip.addReservation(this);
            }
    }

    @PrePersist
    private void prePersist() {
        if (reservationNumber == null || reservationNumber.isBlank()) {
            reservationNumber = generateNumber();
        }
        if (bookingDate == null) {
            bookingDate = LocalDate.now();
        }
        if (status == null) {
            status = ReservationStatus.PENDING;
        }
    }

    private static String generateNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        int randomSuffix = ThreadLocalRandom.current().nextInt(100, 1000);
        return "RES-" + timestamp + "-" + randomSuffix;
    }
}
