package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import travelagency.model.enums.ReservationStatus;
import travelagency.model.enums.UserRole;

/**
 * Admin extends User - demonstrates inheritance hierarchy.
 * Admin can manage trips and reservations.
 */
@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Admin extends User {

    @EqualsAndHashCode.Include
    private Long adminId;

    private String employeeId;

    private String department;

    public Admin(String username, String password, String employeeId, String department) {
        super(username, password, UserRole.ADMIN);
        this.employeeId = employeeId;
        this.department = department;
    }

    // ===== Override abstract method from User =====
    @Override
    public String getDisplayName() {
        return "Admin: " + getUsername() + " (" + department + ")";
    }

    // ===== Admin Management Methods =====

    /**
     * Creates a new trip (business logic placeholder).
     */
    public void createTrip(Trip trip) {
        // In a real app, this might add to a repository
        // Here it demonstrates the method exists per diagram
        if (trip == null) {
            throw new IllegalArgumentException("Trip cannot be null");
        }
    }

    /**
     * Updates an existing trip.
     */
    public void updateTrip(Trip trip) {
        if (trip == null) {
            throw new IllegalArgumentException("Trip cannot be null");
        }
    }

    /**
     * Deletes a trip by ID (business logic placeholder).
     */
    public void deleteTrip(Long tripId) {
        if (tripId == null) {
            throw new IllegalArgumentException("Trip ID cannot be null");
        }
    }

    /**
     * Updates the status of a reservation.
     */
    public void updateReservationStatus(Reservation reservation, ReservationStatus status) {
        if (reservation == null || status == null) {
            throw new IllegalArgumentException("Reservation and status cannot be null");
        }
        reservation.setStatus(status);
    }

    /**
     * Records a payment for a reservation.
     */
    public void recordPayment(Reservation reservation, Payment payment) {
        if (reservation == null || payment == null) {
            throw new IllegalArgumentException("Reservation and payment cannot be null");
        }
        reservation.recordPayment(payment);
    }

    @PrePersist
    private void prePersist() {
        if (adminId == null) {
            adminId = getUserId();
        }
    }

    @PostPersist
    private void postPersist() {
        if (adminId == null) {
            adminId = getUserId();
        }
    }
}
