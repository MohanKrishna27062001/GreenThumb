package dev.mohan.greenthumb.service;

import dev.mohan.greenthumb.dto.WishlistItemRequestDTO;
import dev.mohan.greenthumb.dto.WishlistResponseDTO;

public interface WishlistService {
    WishlistResponseDTO getWishlist();
    WishlistResponseDTO addItem(WishlistItemRequestDTO request);
    WishlistResponseDTO removeItem(Long itemId);
}