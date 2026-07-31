package dev.mohan.greenthumb.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dev.mohan.greenthumb.domain.Plant;
import dev.mohan.greenthumb.domain.User;
import dev.mohan.greenthumb.domain.Wishlist;
import dev.mohan.greenthumb.domain.WishlistItem;
import dev.mohan.greenthumb.dto.WishlistItemDTO;
import dev.mohan.greenthumb.dto.WishlistItemRequestDTO;
import dev.mohan.greenthumb.dto.WishlistResponseDTO;
import dev.mohan.greenthumb.enumeration.WishlistItemStatus;
import dev.mohan.greenthumb.exception.NotFoundException;
import dev.mohan.greenthumb.repository.PlantRepository;
import dev.mohan.greenthumb.repository.UserRepository;
import dev.mohan.greenthumb.repository.WishlistItemRepository;
import dev.mohan.greenthumb.repository.WishlistRepository;
import dev.mohan.greenthumb.service.WishlistService;

@Service
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, WishlistItemRepository wishlistItemRepository, UserRepository userRepository, PlantRepository plantRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
    }
    
    @Override
    public WishlistResponseDTO getWishlist() {
        Wishlist wishlist = getOrCreateActiveWishlist();
        return toWishlistDto(wishlist);
    }

    @Override
    public WishlistResponseDTO addItem(WishlistItemRequestDTO request) {
        Wishlist wishlist = getOrCreateActiveWishlist();
        Plant plant = plantRepository.findById(request.plantId())
                .orElseThrow(() -> new NotFoundException("Plant not found: " + request.plantId()));
                
        WishlistItem item = wishlistItemRepository
                .findByWishlistAndPlantAndStatus(wishlist, plant, WishlistItemStatus.ADDED)
                .orElseGet(() -> {
                    WishlistItem wi = new WishlistItem();
                    wi.setWishlist(wishlist);
                    wi.setPlant(plant);
                    wi.setStatus(WishlistItemStatus.ADDED);
                    return wi;
                });
        wishlistItemRepository.save(item);
        
        return toWishlistDto(wishlist);
    }

    @Override
    public WishlistResponseDTO removeItem(Long itemId) {
        Wishlist wishlist = getOrCreateActiveWishlist();
        WishlistItem item = wishlistItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Wishlist item not found " + itemId));

        if (!item.getWishlist().getId().equals(wishlist.getId())) {
            throw new NotFoundException("Wishlist item not found " + itemId);
        }

        item.setStatus(WishlistItemStatus.REMOVED);
        
        wishlistItemRepository.save(item);

        return toWishlistDto(wishlist);
    }

    // ---- helpers ----

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));
    }

    private WishlistResponseDTO toWishlistDto(Wishlist wishlist) {
        List<WishlistItemDTO> items = wishlistItemRepository
            .findByWishlistAndStatus(wishlist, WishlistItemStatus.ADDED)
            .stream()
            .map(wi -> new WishlistItemDTO(wi.getId(), wi.getPlant().getId(), wi.getPlant().getName()))
            .toList();

        return new WishlistResponseDTO(wishlist.getId(), items);
    }

    private Wishlist getOrCreateActiveWishlist() {
        User user = getCurrentUser();
        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setUser(user);
                    return wishlistRepository.save(wishlist);
                });
    }
}