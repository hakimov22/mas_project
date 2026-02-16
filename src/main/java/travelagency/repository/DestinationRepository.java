package travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelagency.model.Destination;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
}
