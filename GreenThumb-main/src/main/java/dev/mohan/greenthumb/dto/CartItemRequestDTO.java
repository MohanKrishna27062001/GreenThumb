package dev.mohan.greenthumb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDTO(
        @NotNull Long plantId,
        @NotNull @Min(1) Integer quantity
) {}