package dev.mohan.greenthumb.service;

import java.util.function.Supplier;

/**
 * Wraps a state-changing call so that repeating it with the same Idempotency-Key
 * (from the same user, against the same endpoint) replays the first result
 * instead of running the call again.
 */
public interface IdempotencyService {

    /**
     * @param idempotencyKey the client-supplied key identifying "this attempt"
     * @param userEmail      scopes the key to its owner
     * @param endpoint       label for what's being called, e.g. "POST /api/orders"
     * @param responseType   type to deserialize a replayed response into
     * @param action         the actual operation to run on a first attempt
     */
    <T> T execute(String idempotencyKey, String userEmail, String endpoint, Class<T> responseType, Supplier<T> action);
}
