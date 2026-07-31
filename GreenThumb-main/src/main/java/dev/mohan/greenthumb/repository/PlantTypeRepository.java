package dev.mohan.greenthumb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.mohan.greenthumb.domain.PlantType;

/**
 * Extending JpaRepository<PlantType, Long> gives you save / findById / findAll /
 * deleteById / count ... for free. <Entity, type-of-its-@Id>.
 */
@Repository
public interface PlantTypeRepository extends JpaRepository<PlantType, Long> {
}