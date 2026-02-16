package travelagency.model;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Value Object (Embeddable) - identity based on all fields.
 */
@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Address {
    private String street;
    private String city;
    private String postalCode;
    private String country;

    public String getFullAddress() {
        return String.format("%s, %s %s, %s", street, city, postalCode, country);
    }
}
