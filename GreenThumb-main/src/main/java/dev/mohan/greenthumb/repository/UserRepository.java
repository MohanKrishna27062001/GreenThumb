package dev.mohan.greenthumb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.mohan.greenthumb.domain.User;

/**
 * Extending JpaRepository<User, Long> gives you save / findById / findAll /
 * deleteById / count ... for free. <Entity, type-of-its-@Id>.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   // Spring writes: SELECT * FROM app_user WHERE email = ?
    boolean existsByEmail(String email);        // Spring writes: SELECT count(*) > 0 ... WHERE email = ?
}