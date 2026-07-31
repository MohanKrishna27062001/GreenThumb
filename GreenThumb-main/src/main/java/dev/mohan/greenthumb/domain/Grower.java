package dev.mohan.greenthumb.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A Grower — the nursery that grows a plant (e.g. "Sunrise Nurseries").
 * This class IS your ER diagram's "grower" box, in Java.
 */
@Getter                 // Lombok generates getId(), getName(), ...
@Setter                 // Lombok generates setId(...), setName(...), ...
@Entity                 // "This class maps to a database table"
@Table(name = "grower") // the table name
public class Grower extends Auditable {

    @Id                                                 // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-generates it
    private Long id;

    @Column(nullable = false)                           // NOT NULL
    private String name;

    @Column
    private String description;

    @Column(name = "sort_order", nullable = false)      // snake_case in DB, camelCase in Java
    private Integer sortOrder;
}