package travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelagency.model.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
