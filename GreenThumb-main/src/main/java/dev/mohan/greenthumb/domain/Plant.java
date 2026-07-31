// package dev.mohan.greenthumb.domain;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.Getter;
// import lombok.Setter;

// /**
//  * A Plant
//  */
// @Getter                 // Lombok generates getId(), getName(), ...
// @Setter                 // Lombok generates setId(...), setName(...), ...
// @Entity                 // "This class maps to a database table"
// @Table(name = "plant") // the table name
// public class Plant {

//     @Id                                                 // PRIMARY KEY
//     @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-generates it
//     private Long id;

//     @Column(nullable = false)                           // NOT NULL
//     private String name;

//     @Column(nullable= false)                          // NOT NULL
//     private String photo;

//     @Column(name = "original_price", nullable = false)
//     private Double originalPrice;

//     @Column(name = "selling_price",nullable = false)
//     private Double sellingPrice;

//     @Column(name = "sort_order", nullable = false)      // snake_case in DB, camelCase in Java
//     private Integer sortOrder;

//     @ManyToOne
//     @JoinColumn(name = "grower_id", nullable = false)
//     private Grower grower;

//     @ManyToOne
//     @JoinColumn(name = "plant_type_id", nullable = false)
//     private PlantType plantType;

// }



package dev.mohan.greenthumb.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A Plant — a product in the GreenThumb catalog.
 */
@Getter
@Setter
@Entity
@Table(name = "plant")
public class Plant extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String photo;                 // a URL, not the image bytes

    @Column(name = "original_price", nullable = false)
    private Double originalPrice;

    @Column(name = "selling_price", nullable = false)
    private Double sellingPrice;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @ManyToOne
    @JoinColumn(name = "grower_id", nullable = false)
    private Grower grower;                 // the object, column is grower_id

    @ManyToOne
    @JoinColumn(name = "plant_type_id", nullable = false)
    private PlantType plantType;
}