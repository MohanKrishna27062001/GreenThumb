package dev.mohan.greenthumb.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class giving every entity automatic audit timestamps.
 * @MappedSuperclass = not a table itself; its fields drop into each child's table.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)   // fills the dates on save/update
public abstract class Auditable {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)  // set once, never changed
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;
}