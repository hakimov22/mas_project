package travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelagency.model.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmployeeId(String employeeId);
}
