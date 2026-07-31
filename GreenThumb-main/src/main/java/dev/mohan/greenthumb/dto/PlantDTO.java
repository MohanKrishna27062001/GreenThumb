package dev.mohan.greenthumb.dto;

public record PlantDTO(
        Long id,
        String name,
        String photo,
        Double originalPrice,
        Double sellingPrice,
        Integer sortOrder,
        String growerName,       // ← flattened from plant.getGrower().getName()
        String plantTypeName     // ← flattened from plant.getPlantType().getName()
) {}