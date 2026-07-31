package dev.mohan.greenthumb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GrowerRequestDTO(
        @NotBlank String name,        // must be present and not just spaces
        String description,           // optional
        @NotNull Integer sortOrder    // must be present
) {}