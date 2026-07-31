package dev.mohan.greenthumb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.mohan.greenthumb.domain.Plant;
import dev.mohan.greenthumb.domain.Wishlist;
import dev.mohan.greenthumb.domain.WishlistItem;
import dev.mohan.greenthumb.enumeration.WishlistItemStatus;

/**
 * Extending JpaRepository<WishlistItem, Long> gives you save / findById / findAll /
 * deleteById / count ... for free. <Entity, type-of-its-@Id>.
 */
@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByWishlistAndStatus(Wishlist wishlist, WishlistItemStatus status);
    Optional<WishlistItem> findByWishlistAndPlantAndStatus(Wishlist w, Plant p, WishlistItemStatus status);
}