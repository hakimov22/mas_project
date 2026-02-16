package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cultural_trips")
@DiscriminatorValue("Cultural")
@Getter @Setter @NoArgsConstructor
@ToString(callSuper = true)
public class CulturalTrip extends Trip {

    private static final BigDecimal PRICE_MULTIPLIER = new BigDecimal("1.10");
    private boolean guidedTours;
    // Ordered multi-valued attribute: maintains insertion order
    @ElementCollection
    @CollectionTable(name = "cultural_trip_sites", joinColumns = @JoinColumn(name = "trip_id"))
    @OrderColumn(name = "site_order")
    private List<String> historicalSites = new ArrayList<>();

    public CulturalTrip(String code, String name, String desc, Destination dest,
                        LocalDate dep, LocalDate ret, BigDecimal price, int max, boolean guidedTours) {
        super(code, name, desc, dest, dep, ret, price, max);
        this.guidedTours = guidedTours;
    }

    @Override
    public BigDecimal getFinalPrice() {
        return basePrice.multiply(PRICE_MULTIPLIER);
    }

    @Override
    public String getTripType() {
        return "Cultural";
    }

    public void addHistoricalSite(String site) {
        if (site != null && !site.isBlank() && !historicalSites.contains(site))
            historicalSites.add(site);
    }

    // Ordered access: get historical site by index (maintains order)
    public String getHistoricalSite(int index) {
        if (index >= 0 && index < historicalSites.size()) {
            return historicalSites.get(index);
        }
        return null;
    }

    // Get all historical sites in order
    public List<String> getHistoricalSites() {
        return new ArrayList<>(historicalSites); // Returns ordered list
    }
}
