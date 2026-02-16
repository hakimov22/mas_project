package travelagency.model;

import jakarta.persistence.*;
import lombok.*;
import travelagency.model.enums.UserRole;

/**
 * Abstract User class - base for Customer and Admin.
 * Demonstrates abstract class with abstract method (getDisplayName).
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    protected Long userId;

    @Column(unique = true)
    protected String username;

    protected String password;

    @Enumerated(EnumType.STRING)
    protected UserRole role;

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Abstract method - must be implemented by subclasses.
     * Demonstrates polymorphic method call.
     */
    public abstract String getDisplayName();
}
