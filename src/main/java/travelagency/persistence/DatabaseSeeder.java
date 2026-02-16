package travelagency.persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import travelagency.model.*;
import travelagency.model.enums.DifficultyLevel;
import travelagency.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Seeds the database with sample data on application startup.
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final DestinationRepository destinationRepository;
    private final TripRepository tripRepository;
    private final ReservationRepository reservationRepository;
    private final AdminRepository adminRepository;

    public DatabaseSeeder(CustomerRepository customerRepository,
                          DestinationRepository destinationRepository,
                          TripRepository tripRepository,
                          ReservationRepository reservationRepository,
                          AdminRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.destinationRepository = destinationRepository;
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) {
        // Only seed if database is empty
        if (customerRepository.count() > 0) {
            System.out.println("Database already has data, skipping seeding.");
            return;
        }

        System.out.println("Seeding database with sample data...");

        // Create Destinations
        Destination paris = new Destination("Paris", "France", "The City of Light with iconic landmarks", "Temperate");
        Destination alps = new Destination("Swiss Alps", "Switzerland", "Majestic mountain paradise", "Alpine");
        Destination maldives = new Destination("Maldives", "Maldives", "Tropical island paradise", "Tropical");
        Destination rome = new Destination("Rome", "Italy", "Ancient history and culture", "Mediterranean");
        Destination bali = new Destination("Bali", "Indonesia", "Exotic beaches and temples", "Tropical");

        destinationRepository.save(paris);
        destinationRepository.save(alps);
        destinationRepository.save(maldives);
        destinationRepository.save(rome);
        destinationRepository.save(bali);

        // Create Cultural Trips
        CulturalTrip parisTour = new CulturalTrip("CUL-PAR-001", "Paris Cultural Experience",
                "Explore the rich history and art of Paris", paris,
                LocalDate.now().plusDays(30), LocalDate.now().plusDays(37),
                new BigDecimal("1200"), 20, true);
        parisTour.addHistoricalSite("Louvre Museum");
        parisTour.addHistoricalSite("Eiffel Tower");
        parisTour.addHistoricalSite("Notre-Dame Cathedral");

        CulturalTrip romeTour = new CulturalTrip("CUL-ROM-001", "Ancient Rome Discovery",
                "Walk through 2000 years of history", rome,
                LocalDate.now().plusDays(45), LocalDate.now().plusDays(52),
                new BigDecimal("1400"), 15, true);
        romeTour.addHistoricalSite("Colosseum");
        romeTour.addHistoricalSite("Vatican Museums");
        romeTour.addHistoricalSite("Roman Forum");

        tripRepository.save(parisTour);
        tripRepository.save(romeTour);

        // Create Adventure Trips
        AdventureTrip alpsHard = new AdventureTrip("ADV-ALP-001", "Alps Hiking Expedition",
                "Challenge yourself in the Swiss Alps", alps,
                LocalDate.now().plusDays(60), LocalDate.now().plusDays(70),
                new BigDecimal("2500"), 12, DifficultyLevel.HARD, true);

        AdventureTrip alpsEasy = new AdventureTrip("ADV-ALP-002", "Alpine Beginner Trek",
                "Scenic mountain walks for beginners", alps,
                LocalDate.now().plusDays(40), LocalDate.now().plusDays(45),
                new BigDecimal("1500"), 20, DifficultyLevel.EASY, true);

        tripRepository.save(alpsHard);
        tripRepository.save(alpsEasy);

        // Create Vacation Trips
        VacationTrip maldivesVacation = new VacationTrip("VAC-MAL-001", "Maldives Beach Paradise",
                "Ultimate relaxation on pristine beaches", maldives,
                LocalDate.now().plusDays(20), LocalDate.now().plusDays(30),
                new BigDecimal("3500"), 10, "Paradise Island Resort", true);

        VacationTrip baliVacation = new VacationTrip("VAC-BAL-001", "Bali Tropical Escape",
                "Discover the magic of Bali", bali,
                LocalDate.now().plusDays(35), LocalDate.now().plusDays(45),
                new BigDecimal("2800"), 15, "Ubud Luxury Villas", true);

        tripRepository.save(maldivesVacation);
        tripRepository.save(baliVacation);

        // Create Customers
        Address addr1 = new Address("Street 1", "Warsaw", "00001", "Poland");
        Customer customer1 = new Customer("User1", "", "user1@email.com", "555-0001", addr1);

        Address addr2 = new Address("Street 2", "Warsaw", "00002", "Poland");
        Customer customer2 = new Customer("User2", "", "user2@email.com", "555-0002", addr2);

        Address addr3 = new Address("Street 3", "Warsaw", "00003", "Poland");
        Customer customer3 = new Customer("User3", "", "user3@email.com", "555-0003", addr3);

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);

        // Create Sample Reservation
        Reservation reservation = new Reservation(customer1, parisTour, 2);
        reservationRepository.save(reservation);

        // Create Admin (demonstrates User hierarchy)
        Admin admin = new Admin("admin", "admin123", "EMP-001", "Operations");
        adminRepository.save(admin);

        System.out.println("Database seeding completed!");
        System.out.println("  - Destinations: " + destinationRepository.count());
        System.out.println("  - Trips: " + tripRepository.count());
        System.out.println("  - Customers: " + customerRepository.count());
        System.out.println("  - Admins: " + adminRepository.count());
        System.out.println("  - Reservations: " + reservationRepository.count());
    }
}
