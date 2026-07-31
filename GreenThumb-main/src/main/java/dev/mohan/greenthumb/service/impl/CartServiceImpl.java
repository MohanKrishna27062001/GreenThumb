package dev.mohan.greenthumb.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dev.mohan.greenthumb.domain.Cart;
import dev.mohan.greenthumb.domain.CartItem;
import dev.mohan.greenthumb.domain.Plant;
import dev.mohan.greenthumb.domain.User;
import dev.mohan.greenthumb.dto.CartItemDTO;
import dev.mohan.greenthumb.dto.CartItemRequestDTO;
import dev.mohan.greenthumb.dto.CartResponseDTO;
import dev.mohan.greenthumb.enumeration.CartItemStatus;
import dev.mohan.greenthumb.enumeration.CartStatus;
import dev.mohan.greenthumb.exception.NotFoundException;
import dev.mohan.greenthumb.repository.CartItemRepository;
import dev.mohan.greenthumb.repository.CartRepository;
import dev.mohan.greenthumb.repository.PlantRepository;
import dev.mohan.greenthumb.repository.UserRepository;
import dev.mohan.greenthumb.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                           PlantRepository plantRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartResponseDTO getCart() {
        Cart cart = getOrCreateActiveCart();
        return toCartDto(cart);
    }

    @Override
    public CartResponseDTO addItem(CartItemRequestDTO request) {
        Cart cart = getOrCreateActiveCart();
        Plant plant = plantRepository.findById(request.plantId())
                .orElseThrow(() -> new NotFoundException("Plant not found: " + request.plantId()));

        // already in the cart? bump its quantity. otherwise create a new line.
        CartItem item = cartItemRepository
                .findByCartAndPlantAndStatus(cart, plant, CartItemStatus.ADDED)
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setCart(cart);
                    ci.setPlant(plant);
                    ci.setQuantity(0);
                    ci.setStatus(CartItemStatus.ADDED);
                    return ci;
                });

        item.setQuantity(item.getQuantity() + request.quantity());
        item.setAmount(plant.getSellingPrice() * item.getQuantity());   // line total
        cartItemRepository.save(item);

        return toCartDto(cart);
    }

    @Override
    public CartResponseDTO updateItemQuantity(Long itemId, Integer quantity) {
        Cart cart = getOrCreateActiveCart();
        CartItem item = cartItemRepository
                .findById(itemId)
                .orElseThrow(() -> new NotFoundException("Cart item not found: " + itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new NotFoundException("Cart item not found: " + itemId);
        }

        item.setQuantity(quantity);
        item.setAmount(item.getPlant().getSellingPrice() * item.getQuantity());   // line total
        cartItemRepository.save(item);

        return toCartDto(cart);
    }

    @Override
    public CartResponseDTO removeItem(Long itemId) {
        Cart cart = getOrCreateActiveCart();
        CartItem item = cartItemRepository
                .findById(itemId)
                .orElseThrow(() -> new NotFoundException("Cart item not found: " + itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new NotFoundException("Cart item not found: " + itemId);
        }

        item.setStatus(CartItemStatus.REMOVED);

        cartItemRepository.save(item);

        return toCartDto(cart);
    }

    // ---- helpers ----

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));
    }

    private Cart getOrCreateActiveCart() {
        User user = getCurrentUser();
        return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setStatus(CartStatus.ACTIVE);
                    return cartRepository.save(cart);
                });
    }

    private CartResponseDTO toCartDto(Cart cart) {
        List<CartItemDTO> items = cartItemRepository
                .findByCartAndStatus(cart, CartItemStatus.ADDED)
                .stream()
                .map(ci -> new CartItemDTO(ci.getId(), ci.getPlant().getId(),
                        ci.getPlant().getName(), ci.getQuantity(), ci.getAmount()))
                .toList();

        double total = items.stream().mapToDouble(CartItemDTO::amount).sum();
        return new CartResponseDTO(cart.getId(), cart.getStatus().name(), items, total);
    }
}