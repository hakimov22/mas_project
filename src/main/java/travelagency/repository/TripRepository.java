package travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelagency.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
