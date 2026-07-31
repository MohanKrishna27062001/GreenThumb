package dev.mohan.greenthumb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateQuantityDTO(
    @NotNull @Min(1) Integer quantity
) {}