package travelagency.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import travelagency.dto.ReservationDto;
import travelagency.mapper.ReservationMapper;
import travelagency.model.Customer;
import travelagency.model.Reservation;
import travelagency.model.Trip;
import travelagency.repository.CustomerRepository;
import travelagency.repository.ReservationRepository;
import travelagency.repository.TripRepository;

import java.util.*;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final CustomerRepository customerRepository;
    private final TripRepository tripRepository;
    private final ReservationRepository reservationRepository;

    public ReservationController(CustomerRepository customerRepository,
                                 TripRepository tripRepository,
                                 ReservationRepository reservationRepository) {
        this.customerRepository = customerRepository;
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
    }

    @PostMapping
    @Transactional
    public Map<String, Object> createReservation(@RequestBody Map<String, Object> request) {
        long customerId = Long.parseLong(request.get("customerId").toString());
        long tripId = Long.parseLong(request.get("tripId").toString());
        int numberOfPeople = Integer.parseInt(request.get("numberOfPeople").toString());

        Customer customer = customerRepository.findById(customerId).orElse(null);
        Trip trip = tripRepository.findById(tripId).orElse(null);

        if (customer == null || trip == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Customer or Trip not found");
            return error;
        }

        if (numberOfPeople < 1) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Number of people must be at least 1");
            return error;
        }

        if (!trip.hasEnoughSpots(numberOfPeople)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Not enough spots available");
            return error;
        }

        Reservation reservation = new Reservation(customer, trip, numberOfPeople);
        reservationRepository.save(reservation);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("reservationNumber", reservation.getReservationNumber());
        result.put("totalPrice", reservation.getTotalPrice());
        result.put("status", reservation.getStatus().name());
        return result;
    }

    @GetMapping
    @Transactional
    public List<ReservationDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .sorted(Comparator.comparing(Reservation::getReservationId,
                        Comparator.nullsLast(Long::compareTo)).reversed()) // latest first
                .map(ReservationMapper::toDto)
                .toList();
    }

    @PostMapping("/{id}/cancel")
    @Transactional
    public Map<String, Object> cancelReservation(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();
        
        Reservation reservation = reservationRepository.findByReservationNumber(id).orElse(null);

        if (reservation == null) {
            result.put("error", "Reservation not found");
            return result;
        }

        try {
            reservation.cancel();
            reservationRepository.save(reservation);
            result.put("success", true);
            result.put("status", reservation.getStatus().name());
        } catch (IllegalStateException e) {
            result.put("error", e.getMessage());
        }

        return result;
    }

}

