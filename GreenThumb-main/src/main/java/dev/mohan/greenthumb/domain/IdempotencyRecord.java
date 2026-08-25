package dev.mohan.greenthumb.domain;

import dev.mohan.greenthumb.enumeration.IdempotencyStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 * Remembers the outcome of a client's request against a given Idempotency-Key,
 * so a retried request (double-click, network timeout + retry, ...) replays the
 * original result instead of re-running the operation.
 *
 * The unique constraint on (idempotency_key, user_email) is what actually makes
 * this safe under concurrency: if two requests race to claim the same key, the
 * DB rejects the second INSERT and that request is told to back off, instead of
 * both racing through the underlying operation.
 */
@Getter
@Setter
@Entity
@Table(name = "idempotency_record",
        uniqueConstraints = @UniqueConstraint(columnNames = {"idempotency_key", "user_email"}))
public class IdempotencyRecord extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key", nullable = false)
    private String idempotencyKey;

    // scopes the key to its owner: two different users may reuse the same key value
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdempotencyStatus status;

    // JSON snapshot of the response, written once the wrapped call completes
    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;
}
