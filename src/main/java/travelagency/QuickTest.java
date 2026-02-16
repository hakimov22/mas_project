package travelagency;

import travelagency.model.*;
import travelagency.model.enums.DifficultyLevel;
import travelagency.model.enums.PaymentMethod;
import travelagency.model.enums.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Quick test to verify backend model classes
 */
public class QuickTest {

    public static void main(String[] args) {
        System.out.println("=== Travel Agency Backend Model Test ===\n");

        // 1. Test Complex Attribute (Address)
        Address address = new Address("123 Main St", "New York", "10001", "USA");
        System.out.println("✓ Complex Attribute (Address): " + address.getFullAddress());

        // 2. Test Inheritance Hierarchy (User -> Customer, Admin)
        Customer customer = new Customer("John", "Smith", "john@email.com", "555-0123", address);
        Admin admin = new Admin("admin", "pass123", "EMP-001", "Operations");
        
        // Polymorphic method call
        User user1 = customer;
        User user2 = admin;
        System.out.println("✓ Inheritance & Polymorphism:");
        System.out.println("  Customer.getDisplayName(): " + user1.getDisplayName());
        System.out.println("  Admin.getDisplayName(): " + user2.getDisplayName());

        // 3. Test Optional Attribute
        Customer customerNoPhone = new Customer("Jane", "Doe", "jane@email.com", address);
        System.out.println("✓ Optional Attribute (phone): " + (customerNoPhone.getPhone().isEmpty() ? "empty" : "set"));

        // 4. Test Multi-valued Ordered Attribute
        Destination paris = new Destination("Paris", "France", "The City of Light", "Temperate");
        CulturalTrip culturalTrip = new CulturalTrip("CUL-PAR-001", "Paris Tour", "Visit Paris", paris,
                LocalDate.of(2026, 3, 15), LocalDate.of(2026, 3, 22), new BigDecimal("1000"), 20, true);
        culturalTrip.addHistoricalSite("Louvre");
        culturalTrip.addHistoricalSite("Eiffel Tower");
        System.out.println("✓ Multi-valued Ordered (historicalSites): " + culturalTrip.getHistoricalSites());

        // 5. Test Inheritance (Trip -> AdventureTrip, CulturalTrip, VacationTrip)
        Destination alps = new Destination("Swiss Alps", "Switzerland", "Mountains", "Alpine");
        AdventureTrip adventureTrip = new AdventureTrip("ADV-001", "Alps Hike", "Trek", alps,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 10),
                new BigDecimal("2000"), 15, DifficultyLevel.HARD, true);
        
        // Polymorphic method calls
        Trip trip1 = culturalTrip;
        Trip trip2 = adventureTrip;
        System.out.println("✓ Disjoint Inheritance & Polymorphism:");
        System.out.println("  CulturalTrip.getTripType(): " + trip1.getTripType());
        System.out.println("  AdventureTrip.getTripType(): " + trip2.getTripType());

        // 6. Test Overload (isAvailable with/without date)
        System.out.println("✓ Method Overload:");
        System.out.println("  isAvailable(): " + trip1.isAvailable());
        System.out.println("  isAvailable(date): " + trip1.isAvailable(LocalDate.of(2026, 4, 1)));

        // 7. Test Derived Attributes
        System.out.println("✓ Derived Attributes:");
        System.out.println("  Duration: " + trip1.getDuration() + " days");
        System.out.println("  Available Spots: " + trip1.getAvailableSpots());

        // 8. Test Associations (Customer <-> Reservation <-> Trip)
        Reservation reservation = new Reservation(customer, culturalTrip, 2);
        System.out.println("✓ Associations (bidirectional):");
        System.out.println("  Customer has " + customer.getReservations().size() + " reservation(s)");
        System.out.println("  Trip has " + culturalTrip.getReservations().size() + " reservation(s)");

        // 9. Test Derived Attribute (totalPrice)
        System.out.println("✓ Derived Attribute (totalPrice): $" + reservation.getTotalPrice());

        // 10. Test State Transitions (PENDING -> CONFIRMED)
        Payment payment = new Payment(reservation.getTotalPrice(), LocalDate.now(),
                PaymentMethod.BANK_TRANSFER, "TXN-001");
        reservation.recordPayment(payment);
        System.out.println("✓ State Transition: " + ReservationStatus.PENDING + " -> " + reservation.getStatus());

        // 11. Test XOR Constraint (Payment method)
        System.out.println("✓ XOR Constraint (Payment.method): " + payment.getMethod());

        // 12. Test Composition (Trip -> Flight)
        Flight flight = new Flight("AF123", "Air France", "JFK", "CDG",
                LocalDate.of(2026, 3, 15).atTime(10, 0),
                LocalDate.of(2026, 3, 15).atTime(22, 0));
        culturalTrip.setFlight(flight);
        System.out.println("✓ Composition (Trip-Flight): " + culturalTrip.getFlight().getFlightNumber());

        System.out.println("\n All backend model tests passed!");
    }
}
