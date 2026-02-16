package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "trip")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long flightId;
    private String flightNumber;
    private String airline;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    @OneToOne
    @JoinColumn(name = "trip_id", unique = true)
    private Trip trip;

    public Flight(String flightNumber, String airline,
                  String departureAirport, String arrivalAirport,
                  LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Duration getFlightDuration() {
        return Duration.between(departureTime, arrivalTime);
    }
}
