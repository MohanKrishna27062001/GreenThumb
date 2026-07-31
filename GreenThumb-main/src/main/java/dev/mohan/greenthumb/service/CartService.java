package dev.mohan.greenthumb.service;

import dev.mohan.greenthumb.dto.CartItemRequestDTO;
import dev.mohan.greenthumb.dto.CartResponseDTO;

public interface CartService {
    CartResponseDTO getCart();
    CartResponseDTO addItem(CartItemRequestDTO request);
    CartResponseDTO updateItemQuantity(Long itemId, Integer quantity);
    CartResponseDTO removeItem(Long itemId);
}