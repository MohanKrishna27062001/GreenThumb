package dev.mohan.greenthumb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.mohan.greenthumb.domain.Cart;
import dev.mohan.greenthumb.domain.User;
import dev.mohan.greenthumb.enumeration.CartStatus;

/**
 * Extending JpaRepository<Cart, Long> gives you save / findById / findAll /
 * deleteById / count ... for free. <Entity, type-of-its-@Id>.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAndStatus(User user, CartStatus status);
}