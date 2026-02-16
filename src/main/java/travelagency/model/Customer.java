package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import travelagency.model.enums.ReservationStatus;
import travelagency.model.enums.UserRole;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer extends User - demonstrates inheritance hierarchy.
 */
@Entity
@Table(name = "customers")
@DiscriminatorValue("CUSTOMER")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, exclude = "reservations")
public class Customer extends User {

    @EqualsAndHashCode.Include
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @Embedded
    private Address address;
    private LocalDate registrationDate;
    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    // ===== Constructor Overloading =====
    public Customer(String firstName, String lastName, String email, String phone, Address address) {
        super(email, "", UserRole.CUSTOMER); // username = email, no password by default
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = LocalDate.now();
    }

    // Overloaded constructor: Customer without phone number
    public Customer(String firstName, String lastName, String email, Address address) {
        this(firstName, lastName, email, "", address);
    }

    // ===== Override abstract method from User =====
    @Override
    public String getDisplayName() {
        return getFullName();
    }

    // ===== Business Methods =====
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean hasBookedTrip(Trip trip) {
        return reservations.stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .anyMatch(r -> r.getTrip().equals(trip));
    }

    // ===== Association: Customer <-> Reservation =====
    public void addReservation(Reservation r) {
        if (!reservations.contains(r)) {
            reservations.add(r);
            if (r.getCustomer() != this) r.setCustomer(this);
        }
    }

    public void removeReservation(Reservation r) {
        if (reservations.remove(r) && r.getCustomer() == this) r.setCustomer(null);
    }

    @PrePersist
    private void prePersist() {
        if (registrationDate == null) {
            registrationDate = LocalDate.now();
        }
        if (customerId == null) {
            customerId = getUserId();
        }
    }

    @PostPersist
    private void postPersist() {
        if (customerId == null) {
            customerId = getUserId();
        }
    }
}
