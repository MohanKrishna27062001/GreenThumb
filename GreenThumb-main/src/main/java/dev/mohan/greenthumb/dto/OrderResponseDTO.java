package dev.mohan.greenthumb.dto;

import java.util.List;

public record OrderResponseDTO (
    Long id, 
    String orderId, 
    String status, 
    Double totalAmount, 
    List<OrderItemDTO> items
) {}