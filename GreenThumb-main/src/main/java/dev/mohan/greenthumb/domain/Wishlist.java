package dev.mohan.greenthumb.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A Wishlist
 */
@Getter                 // Lombok generates getId(), getName(), ...
@Setter                 // Lombok generates setId(...), setName(...), ...
@Entity                 // "This class maps to a database table"
@Table(name = "wishlist") // the table name
public class Wishlist extends Auditable {

    @Id                                                 // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-generates it
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}