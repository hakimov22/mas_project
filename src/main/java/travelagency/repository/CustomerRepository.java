package travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelagency.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
