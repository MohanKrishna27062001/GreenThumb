package dev.mohan.greenthumb.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.mohan.greenthumb.dto.OrderResponseDTO;
import dev.mohan.greenthumb.service.IdempotencyService;
import dev.mohan.greenthumb.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final IdempotencyService idempotencyService;

    public OrderController(OrderService orderService, IdempotencyService idempotencyService) {
        this.orderService = orderService;
        this.idempotencyService = idempotencyService;
    }

    @GetMapping
    public List<OrderResponseDTO> getMyOrders() {
        return orderService.getMyOrders();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    // POST /api/orders = "check out my cart". Pass an Idempotency-Key header
    // (e.g. a UUID generated once per checkout attempt on the client) so that a
    // retried request - double-click, timeout + retry - replays the first order
    // instead of creating a second one. The header is optional: without it the
    // checkout just runs normally, with no replay protection.
    @PostMapping
    public ResponseEntity<OrderResponseDTO> checkout(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        OrderResponseDTO order = (idempotencyKey == null || idempotencyKey.isBlank())
                ? orderService.checkout()
                : idempotencyService.execute(idempotencyKey, currentUserEmail(), "POST /api/orders",
                        OrderResponseDTO.class, orderService::checkout);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    private String currentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
