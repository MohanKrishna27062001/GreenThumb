package dev.mohan.greenthumb.domain;

import dev.mohan.greenthumb.enumeration.WishlistItemStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A WishlistItem
 */
@Getter                 // Lombok generates getId(), getName(), ...
@Setter                 // Lombok generates setId(...), setName(...), ...
@Entity                 // "This class maps to a database table"
@Table(name = "wishlist_item") // the table name
public class WishlistItem extends Auditable {

    @Id                                                 // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-generates it
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WishlistItemStatus status;
}