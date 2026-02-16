package travelagency.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import travelagency.dto.CustomerDto;
import travelagency.dto.CustomerReservationDto;
import travelagency.mapper.CustomerMapper;
import travelagency.model.Reservation;
import travelagency.repository.CustomerRepository;

import java.util.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable long id) {
        return customerRepository.findById(id)
                .map(CustomerMapper::toDto)
                .orElse(null);
    }

    @Transactional
    @GetMapping("/{id}/reservations")
    public List<CustomerReservationDto> getCustomerReservations(@PathVariable long id) {
        return customerRepository.findById(id)
                .map(customer -> customer.getReservations().stream()
                        .sorted(Comparator.comparing(Reservation::getReservationId,
                                Comparator.nullsLast(Long::compareTo)).reversed()) // latest first
                        .map(CustomerMapper::toReservationDto)
                        .toList())
                .orElse(Collections.emptyList());
    }
}

