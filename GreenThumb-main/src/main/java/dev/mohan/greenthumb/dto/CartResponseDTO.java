package dev.mohan.greenthumb.dto;

import java.util.List;

public record CartResponseDTO(
        Long cartId,
        String status,
        List<CartItemDTO> items,
        Double totalAmount
) {}