package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "destinations")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "trips")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long destinationId;
    private String name;
    private String country;
    private String description;
    private String climate;
    @OneToMany(mappedBy = "destination", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Trip> trips = new ArrayList<>();

    public Destination(String name, String country, String description, String climate) {
        this.name = name;
        this.country = country;
        this.description = description;
        this.climate = climate;
    }

    // ===== Association: Destination <-> Trip =====
    public void addTrip(Trip trip) {
        if (!trips.contains(trip)) {
            trips.add(trip);
            if (trip.getDestination() != this) trip.setDestination(this);
        }
    }

    public void removeTrip(Trip trip) {
        if (trips.remove(trip) && trip.getDestination() == this) trip.setDestination(null);
    }

    public List<Trip> getAvailableTrips() {
        return trips.stream().filter(Trip::isAvailable).toList();
    }
}
