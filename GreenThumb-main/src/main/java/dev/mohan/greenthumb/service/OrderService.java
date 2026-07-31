package dev.mohan.greenthumb.service;

import java.util.List;

import dev.mohan.greenthumb.dto.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO checkout();
    List<OrderResponseDTO> getMyOrders();
    OrderResponseDTO getOrder(Long id);
}