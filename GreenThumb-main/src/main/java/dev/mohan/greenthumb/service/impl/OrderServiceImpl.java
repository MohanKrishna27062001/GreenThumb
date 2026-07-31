package dev.mohan.greenthumb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.mohan.greenthumb.domain.Cart;
import dev.mohan.greenthumb.domain.CartItem;
import dev.mohan.greenthumb.domain.OrderItem;
import dev.mohan.greenthumb.domain.OrderSummary;
import dev.mohan.greenthumb.domain.User;
import dev.mohan.greenthumb.dto.OrderItemDTO;
import dev.mohan.greenthumb.dto.OrderResponseDTO;
import dev.mohan.greenthumb.enumeration.CartItemStatus;
import dev.mohan.greenthumb.enumeration.CartStatus;
import dev.mohan.greenthumb.enumeration.OrderSummaryStatus;
import dev.mohan.greenthumb.exception.BadRequestException;
import dev.mohan.greenthumb.exception.NotFoundException;
import dev.mohan.greenthumb.repository.CartItemRepository;
import dev.mohan.greenthumb.repository.CartRepository;
import dev.mohan.greenthumb.repository.OrderItemRepository;
import dev.mohan.greenthumb.repository.OrderSummaryRepository;
import dev.mohan.greenthumb.repository.UserRepository;
import dev.mohan.greenthumb.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderSummaryRepository orderSummaryRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                            OrderSummaryRepository orderSummaryRepository, OrderItemRepository orderItemRepository,
                            UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderSummaryRepository = orderSummaryRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional      // ← all writes below commit together, or all roll back
    public OrderResponseDTO checkout() {
        User user = getCurrentUser();

        // 1. the user's active cart + its items
        Cart cart = cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseThrow(() -> new BadRequestException("No active cart to check out"));
        List<CartItem> cartItems = cartItemRepository.findByCartAndStatus(cart, CartItemStatus.ADDED);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // 2. create the order shell
        OrderSummary order = new OrderSummary();
        order.setOrderId("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setStatus(OrderSummaryStatus.PENDING);
        order.setUser(user);
        order.setCart(cart);
        order.setTotalAmount(0.0);           // temp; set after we sum lines
        orderSummaryRepository.save(order);  // save first so it has an id

        // 3. one OrderItem per cart line, freezing the price
        double total = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrderSummary(order);
            oi.setPlant(ci.getPlant());
            oi.setQuantity(ci.getQuantity());
            oi.setSoldPrice(ci.getPlant().getSellingPrice());   // ← SNAPSHOT the price now
            orderItems.add(oi);
            total += oi.getSoldPrice() * oi.getQuantity();
        }
        orderItemRepository.saveAll(orderItems);

        // 4. finalize total + close the cart
        order.setTotalAmount(total);
        orderSummaryRepository.save(order);
        cart.setStatus(CartStatus.ORDERED);   // this cart is done; next add-to-cart makes a fresh one
        cartRepository.save(cart);

        return toDto(order, orderItems);
    }

    @Override
    public List<OrderResponseDTO> getMyOrders() {
        User user = getCurrentUser();

        // all orders belonging to this user
        List<OrderSummary> orders = orderSummaryRepository.findByUser(user);

        return orders.stream()
                .map(order -> {                                                  // ← block lambda: does 2 steps
                    List<OrderItem> items = orderItemRepository.findByOrderSummary(order);  // 1. fetch its items
                    return toDto(order, items);                                  // 2. build the DTO
                })
                .toList();
    }

    @Override
    public OrderResponseDTO getOrder(Long id) {
        User user = getCurrentUser();

        // 1. does the order exist at all?
        OrderSummary order = orderSummaryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));

        // 2. is it THIS user's order? (don't let people read others' orders)
        if (!order.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Order not found: " + id);
        }

        // 3. fetch its items and build the DTO
        List<OrderItem> items = orderItemRepository.findByOrderSummary(order);
        return toDto(order, items);
    }

    // ---- helpers ----
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));
    }

    private OrderResponseDTO toDto(OrderSummary order, List<OrderItem> items) {
        List<OrderItemDTO> itemDtos = items.stream()
                .map(oi -> new OrderItemDTO(oi.getId(), oi.getPlant().getId(),
                        oi.getPlant().getName(), oi.getQuantity(), oi.getSoldPrice()))
                .toList();
        return new OrderResponseDTO(order.getId(), order.getOrderId(),
                order.getStatus().name(), order.getTotalAmount(), itemDtos);
    }
}