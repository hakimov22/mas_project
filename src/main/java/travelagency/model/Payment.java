package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import travelagency.model.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Payment class with XOR constraint.
 * 
 * XOR Constraint: Payment method must be EXACTLY ONE of:
 * - CASH (exclusive)
 * - BANK_TRANSFER (exclusive)
 * 
 * Cannot have multiple payment methods simultaneously (XOR constraint).
 */
@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "reservation")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long paymentId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod method; // XOR: exactly one method (enum enforces this)
    private LocalDate paymentDate;
    private String transactionReference;
    @OneToOne
    @JoinColumn(name = "reservation_id", unique = true)
    private Reservation reservation;

    public Payment(BigDecimal amount, LocalDate paymentDate,
                   PaymentMethod method, String transactionReference) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        // XOR constraint: method enum ensures exactly one value
        if (method == null) {
            throw new IllegalArgumentException("Payment method is required (XOR constraint)");
        }
        this.method = method;
        this.transactionReference = transactionReference;
    }

    public void validate() {
        if (!isValid()) throw new IllegalStateException("Payment is not valid");
    }

    public boolean isValid() {
        // XOR constraint validation: method must be exactly one (not null)
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0
                && method != null && paymentDate != null; // method != null enforces XOR
    }

    /**
     * XOR constraint: Payment method can only be changed to another single method.
     * Cannot have multiple methods simultaneously.
     */
    public void setMethod(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null (XOR constraint)");
        }
        this.method = method; // Replaces previous method (XOR)
    }
}
