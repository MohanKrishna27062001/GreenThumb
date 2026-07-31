package dev.mohan.greenthumb.dto;

public record WishlistItemDTO(
    Long id, 
    Long plantId, 
    String plantName
) {}  