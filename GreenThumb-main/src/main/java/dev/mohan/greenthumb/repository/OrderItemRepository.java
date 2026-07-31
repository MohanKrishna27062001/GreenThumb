package dev.mohan.greenthumb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.mohan.greenthumb.domain.OrderItem;
import dev.mohan.greenthumb.domain.OrderSummary;

/**
 * Extending JpaRepository<OrderItem, Long> gives you save / findById / findAll /
 * deleteById / count ... for free. <Entity, type-of-its-@Id>.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderSummary(OrderSummary orderSummary);
}