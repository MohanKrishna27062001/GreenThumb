package dev.mohan.greenthumb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.mohan.greenthumb.domain.Grower;
import dev.mohan.greenthumb.dto.GrowerDTO;
import dev.mohan.greenthumb.dto.GrowerRequestDTO;
import dev.mohan.greenthumb.exception.NotFoundException;
import dev.mohan.greenthumb.repository.GrowerRepository;
import dev.mohan.greenthumb.service.GrowerService;

@Service   // marks this as a Spring-managed service bean
public class GrowerServiceImpl implements GrowerService {

    private final GrowerRepository growerRepository;

    // Constructor injection: Spring sees this service needs a GrowerRepository
    // and hands one in automatically. You never call "new" yourself.
    public GrowerServiceImpl(GrowerRepository growerRepository) {
        this.growerRepository = growerRepository;
    }

    @Override
    public List<GrowerDTO> getAllGrowers() {
        return growerRepository.findAll()   // List<Grower> from the DB
                .stream()
                .map(this::toDto)           // convert each entity → DTO
                .toList();
    }

    @Override
    public GrowerDTO getGrowerById(Long id) {
        Grower grower = growerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Grower not found: " + id));
        return toDto(grower);
    }

    @Override
    public GrowerDTO createGrower(GrowerRequestDTO request) {
        Grower grower = new Grower();
        grower.setName(request.name());
        grower.setDescription(request.description());
        grower.setSortOrder(request.sortOrder());

        Grower saved = growerRepository.save(grower);   // DB assigns id + created_date
        return toDto(saved);                            // return the full output DTO
    }

    @Override
    public GrowerDTO updateGrower(Long id, GrowerRequestDTO request) {
        Grower grower = growerRepository.findById(id)                       // 1. fetch existing
                .orElseThrow(() -> new NotFoundException("Grower not found: " + id));

        grower.setName(request.name());                                    // 2. modify its fields
        grower.setDescription(request.description());
        grower.setSortOrder(request.sortOrder());

        Grower saved = growerRepository.save(grower);                      // 3. save (runs an UPDATE)
        return toDto(saved);
    }

    @Override
    public void deleteGrower(Long id) {
        if (!growerRepository.existsById(id)) {                            // 404 if it doesn't exist
            throw new NotFoundException("Grower not found: " + id);
        }
        growerRepository.deleteById(id);
    }

    // private helper: entity → DTO (we'll automate this with MapStruct later)
    private GrowerDTO toDto(Grower g) {
        return new GrowerDTO(g.getId(), g.getName(), g.getDescription(), g.getSortOrder());
    }
}