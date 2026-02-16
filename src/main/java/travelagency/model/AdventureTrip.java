package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import travelagency.model.enums.DifficultyLevel;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "adventure_trips")
@DiscriminatorValue("Adventure")
@Getter @Setter @NoArgsConstructor
@ToString(callSuper = true)
public class AdventureTrip extends Trip {

    private static final BigDecimal PRICE_MULTIPLIER = new BigDecimal("1.30");
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
    private boolean equipmentIncluded;

    public AdventureTrip(String code, String name, String desc, Destination dest,
            LocalDate dep, LocalDate ret, BigDecimal price, int max,
                         DifficultyLevel difficultyLevel, boolean equipmentIncluded) {
        super(code, name, desc, dest, dep, ret, price, max);
        this.difficultyLevel = difficultyLevel;
        this.equipmentIncluded = equipmentIncluded;
    }

    @Override
    public BigDecimal getFinalPrice() {
        return basePrice.multiply(PRICE_MULTIPLIER);
    }

    @Override
    public String getTripType() {
        return "Adventure";
    }
}
