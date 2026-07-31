package dev.mohan.greenthumb.dto;

/**
 * What the API exposes about a PlantType. Notice: no audit fields, no relationships —
 * just what a client needs.
 */
public record PlantTypeDTO(
        Long id,
        String name,
        String description,
        Integer sortOrder
) {}