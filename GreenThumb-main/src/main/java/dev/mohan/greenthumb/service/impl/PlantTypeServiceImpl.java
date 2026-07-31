package dev.mohan.greenthumb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.mohan.greenthumb.domain.PlantType;
import dev.mohan.greenthumb.dto.PlantTypeDTO;
import dev.mohan.greenthumb.dto.PlantTypeRequestDTO;
import dev.mohan.greenthumb.exception.NotFoundException;
import dev.mohan.greenthumb.repository.PlantTypeRepository;
import dev.mohan.greenthumb.service.PlantTypeService;

@Service   // marks this as a Spring-managed service bean
public class PlantTypeServiceImpl implements PlantTypeService {

    private final PlantTypeRepository plantTypeRepository;

    // Constructor injection: Spring sees this service needs a PlantTypeRepository
    // and hands one in automatically. You never call "new" yourself.
    public PlantTypeServiceImpl(PlantTypeRepository plantTypeRepository) {
        this.plantTypeRepository = plantTypeRepository;
    }

    @Override
    public List<PlantTypeDTO> getAllPlantTypes() {
        return plantTypeRepository.findAll() // List<PlantType> from the DB
                .stream()
                .map(this::toDto) // convert each entity → DTO
                .toList();
    }

    @Override
    public PlantTypeDTO createPlantType(PlantTypeRequestDTO request) {
        PlantType plantType = new PlantType();
        plantType.setName(request.name());
        plantType.setDescription(request.description());
        plantType.setSortOrder(request.sortOrder());

        PlantType saved = plantTypeRepository.save(plantType);   // DB assigns id + created_date
        return toDto(saved);                            // return the full output DTO
    }

    @Override
    public PlantTypeDTO getPlantTypeById(Long id) {
        PlantType plantType = plantTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("PlantType not found: " + id));
        return toDto(plantType);
    }

    @Override
    public PlantTypeDTO updatePlantType(Long id, PlantTypeRequestDTO request) {
        PlantType plantType = plantTypeRepository.findById(id)                       // 1. fetch existing
                .orElseThrow(() -> new NotFoundException("PlantType not found: " + id));

        plantType.setName(request.name());                                    // 2. modify its fields
        plantType.setDescription(request.description());
        plantType.setSortOrder(request.sortOrder());

        PlantType saved = plantTypeRepository.save(plantType);                      // 3. save (runs an UPDATE)
        return toDto(saved);
    }

    @Override
    public void deletePlantType(Long id) {
        if (!plantTypeRepository.existsById(id)) {                            // 404 if it doesn't exist
            throw new NotFoundException("PlantType not found: " + id);
        }
        plantTypeRepository.deleteById(id);
    }

    // private helper: entity → DTO (we'll automate this with MapStruct later)
    private PlantTypeDTO toDto(PlantType pt) {
        return new PlantTypeDTO(pt.getId(), pt.getName(), pt.getDescription(), pt.getSortOrder());
    }
}
