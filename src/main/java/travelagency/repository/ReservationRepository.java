package travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelagency.model.Reservation;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationNumber(String reservationNumber);
}
