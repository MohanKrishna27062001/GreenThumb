package dev.mohan.greenthumb.dto;

/**
 * What the API exposes about a Grower. Notice: no audit fields, no relationships —
 * just what a client needs.
 */
public record GrowerDTO(
        Long id,
        String name,
        String description,
        Integer sortOrder
) {}