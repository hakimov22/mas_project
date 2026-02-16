package travelagency.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import travelagency.dto.TripDto;
import travelagency.dto.TripReservationDto;
import travelagency.mapper.TripMapper;
import travelagency.model.Reservation;
import travelagency.repository.TripRepository;

import java.util.*;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
public class TripController {

    private final TripRepository tripRepository;

    public TripController(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @GetMapping
    public List<TripDto> getAllTrips() {
        return tripRepository.findAll().stream()
                .map(TripMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public TripDto getTripById(@PathVariable long id) {
        return tripRepository.findById(id)
                .map(TripMapper::toDto)
                .orElse(null);
    }

    @Transactional
    @GetMapping("/{id}/reservations")
    public List<TripReservationDto> getTripReservations(@PathVariable long id) {
        return tripRepository.findById(id)
                .map(trip -> trip.getReservations().stream()
                        .sorted(Comparator.comparing(Reservation::getReservationId,
                                Comparator.nullsLast(Long::compareTo)).reversed()) // latest first
                        .map(TripMapper::toReservationDto)
                        .toList())
                .orElse(Collections.emptyList());
    }
}

