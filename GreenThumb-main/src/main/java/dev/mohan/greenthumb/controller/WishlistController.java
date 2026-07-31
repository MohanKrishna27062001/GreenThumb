package dev.mohan.greenthumb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.mohan.greenthumb.dto.WishlistItemRequestDTO;
import dev.mohan.greenthumb.dto.WishlistResponseDTO;
import dev.mohan.greenthumb.service.WishlistService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public WishlistResponseDTO getWishlist() {
        return wishlistService.getWishlist();
    }

    @PostMapping("/items")
    public ResponseEntity<WishlistResponseDTO> addItem(@Valid @RequestBody WishlistItemRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistService.addItem(request));
    }

    @DeleteMapping("/items/{id}")
    public WishlistResponseDTO delete(@PathVariable Long id) {
        return wishlistService.removeItem(id);
    }
}