package dev.mohan.greenthumb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.mohan.greenthumb.dto.CartItemRequestDTO;
import dev.mohan.greenthumb.dto.CartResponseDTO;
import dev.mohan.greenthumb.dto.UpdateQuantityDTO;
import dev.mohan.greenthumb.service.CartService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartResponseDTO getCart() {
        return cartService.getCart();
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItem(@Valid @RequestBody CartItemRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(request));
    }

    @PutMapping("/items/{id}")
    public CartResponseDTO updateItemQuantity(@PathVariable Long id, @Valid @RequestBody UpdateQuantityDTO request) {
        return cartService.updateItemQuantity(id, request.quantity());
    }

    @DeleteMapping("/items/{id}")                   // DELETE /api/cart/items/5
    public CartResponseDTO delete(@PathVariable Long id) {
        return cartService.removeItem(id);
    }
}