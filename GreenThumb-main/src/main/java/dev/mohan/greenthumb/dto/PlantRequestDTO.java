package dev.mohan.greenthumb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlantRequestDTO(
        @NotBlank String name,
        @NotBlank String photo,
        @NotNull Double originalPrice,
        @NotNull Double sellingPrice,
        @NotNull Integer sortOrder,
        @NotNull Long growerId,
        @NotNull Long plantTypeId
) {}