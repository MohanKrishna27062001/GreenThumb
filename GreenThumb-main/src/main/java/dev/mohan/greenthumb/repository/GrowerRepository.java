package dev.mohan.greenthumb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.mohan.greenthumb.domain.Grower;

/**
 * Extending JpaRepository<Grower, Long> gives you save / findById / findAll /
 * deleteById / count ... for free. <Entity, type-of-its-@Id>.
 */
@Repository
public interface GrowerRepository extends JpaRepository<Grower, Long> {
}