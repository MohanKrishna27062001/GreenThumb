package dev.mohan.greenthumb.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A Role
 */
@Getter                 // Lombok generates getId(), getName(), ...
@Setter                 // Lombok generates setId(...), setName(...), ...
@Entity                 // "This class maps to a database table"
@Table(name = "role") // the table name
public class Role {

    @Id                                                 // PRIMARY KEY
    @Column(length = 50)
    private String name;

}