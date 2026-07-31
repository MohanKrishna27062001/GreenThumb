package dev.mohan.greenthumb.dto;

import jakarta.validation.constraints.NotNull;

public record WishlistItemRequestDTO(
    @NotNull Long plantId       
) {}   