package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vacation_trips")
@DiscriminatorValue("Vacation")
@Getter @Setter @NoArgsConstructor
@ToString(callSuper = true)
public class VacationTrip extends Trip {

    private static final BigDecimal PRICE_MULTIPLIER = new BigDecimal("1.50");
    private String resortName;
    private boolean allInclusive;

    public VacationTrip(String code, String name, String desc, Destination dest,
            LocalDate dep, LocalDate ret, BigDecimal price, int max,
                        String resortName, boolean allInclusive) {
        super(code, name, desc, dest, dep, ret, price, max);
        this.resortName = resortName;
        this.allInclusive = allInclusive;
    }

    @Override
    public BigDecimal getFinalPrice() {
        return basePrice.multiply(PRICE_MULTIPLIER);
    }

    @Override
    public String getTripType() {
        return "Vacation";
    }
}
