package dev.mohan.greenthumb.service.impl;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import dev.mohan.greenthumb.domain.IdempotencyRecord;
import dev.mohan.greenthumb.enumeration.IdempotencyStatus;
import dev.mohan.greenthumb.exception.BadRequestException;
import dev.mohan.greenthumb.exception.ConflictException;
import dev.mohan.greenthumb.repository.IdempotencyRecordRepository;
import dev.mohan.greenthumb.service.IdempotencyService;

@Service
public class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyRecordRepository repository;
    private final ObjectMapper objectMapper;

    public IdempotencyServiceImpl(IdempotencyRecordRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T execute(String idempotencyKey, String userEmail, String endpoint, Class<T> responseType,
            Supplier<T> action) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new BadRequestException("Idempotency-Key header must not be blank");
        }

        // Note: this class is deliberately NOT @Transactional. Each repository call
        // below commits on its own, so the IN_PROGRESS row is visible to concurrent
        // requests immediately, and survives even if `action` later fails.

        Optional<IdempotencyRecord> existing = repository.findByIdempotencyKeyAndUserEmail(idempotencyKey, userEmail);
        if (existing.isPresent()) {
            return replay(existing.get(), responseType);
        }

        IdempotencyRecord record = new IdempotencyRecord();
        record.setIdempotencyKey(idempotencyKey);
        record.setUserEmail(userEmail);
        record.setEndpoint(endpoint);
        record.setStatus(IdempotencyStatus.IN_PROGRESS);
        try {
            repository.save(record);   // unique constraint claims the key; commits before this call returns
        } catch (DataIntegrityViolationException e) {
            // lost the race: another request with this exact key is already running
            throw new ConflictException("A request with this Idempotency-Key is already in progress");
        }

        try {
            T result = action.get();
            record.setStatus(IdempotencyStatus.COMPLETED);
            record.setResponseBody(writeJson(result));
            repository.save(record);
            return result;
        } catch (RuntimeException ex) {
            // free the key so a genuine retry after a failure isn't locked out forever
            repository.delete(record);
            throw ex;
        }
    }

    private <T> T replay(IdempotencyRecord record, Class<T> responseType) {
        if (record.getStatus() == IdempotencyStatus.IN_PROGRESS) {
            throw new ConflictException("A request with this Idempotency-Key is already in progress");
        }
        try {
            return objectMapper.readValue(record.getResponseBody(), responseType);
        } catch (JacksonException e) {
            throw new IllegalStateException("Could not replay cached idempotent response", e);
        }
    }

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JacksonException e) {
            throw new IllegalStateException("Could not cache idempotent response", e);
        }
    }
}
