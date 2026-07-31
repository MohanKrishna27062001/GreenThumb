package dev.mohan.greenthumb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.mohan.greenthumb.domain.Grower;
import dev.mohan.greenthumb.domain.Plant;
import dev.mohan.greenthumb.domain.PlantType;
import dev.mohan.greenthumb.dto.PlantDTO;
import dev.mohan.greenthumb.dto.PlantRequestDTO;
import dev.mohan.greenthumb.exception.NotFoundException;
import dev.mohan.greenthumb.repository.GrowerRepository;
import dev.mohan.greenthumb.repository.PlantRepository;
import dev.mohan.greenthumb.repository.PlantTypeRepository;
import dev.mohan.greenthumb.service.PlantService;

@Service   // marks this as a Spring-managed service bean
public class PlantServiceImpl implements PlantService {

    private final PlantRepository plantRepository;
    private final GrowerRepository growerRepository;
    private final PlantTypeRepository plantTypeRepository;

    // Constructor injection: Spring sees this service needs a PlantTypeRepository
    // and hands one in automatically. You never call "new" yourself.
    public PlantServiceImpl(PlantRepository plantRepository, GrowerRepository growerRepository, PlantTypeRepository plantTypeRepository) {
        this.plantRepository = plantRepository;
        this.growerRepository = growerRepository;
        this.plantTypeRepository = plantTypeRepository;
    }

    @Override
    public List<PlantDTO> getAllPlants() {
        return plantRepository.findAll()   // List<Plant> from the DB
                .stream()
                .map(this::toDto)           // convert each entity → DTO
                .toList();
    }

    @Override
    public PlantDTO getPlantById(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plant not found: " + id));
        return toDto(plant);
    }

    @Override
    public PlantDTO createPlant(PlantRequestDTO request) {
        Plant plant = new Plant();
        plant.setName(request.name());
        plant.setPhoto(request.photo());
        plant.setOriginalPrice(request.originalPrice());
        plant.setSellingPrice(request.sellingPrice());
        plant.setSortOrder(request.sortOrder());
        Grower grower = growerRepository.findById(request.growerId()).orElseThrow(() -> new NotFoundException("Grower not found: " + request.growerId()));
        plant.setGrower(grower);
        PlantType plantType = plantTypeRepository.findById(request.plantTypeId()).orElseThrow(() -> new NotFoundException("Plant Type not found: " + request.plantTypeId()));
        plant.setPlantType(plantType);
        
        Plant saved = plantRepository.save(plant);   // DB assigns id + created_date

        return toDto(saved);                            // return the full output DTO
    }

    @Override
    public PlantDTO updatePlant(Long id, PlantRequestDTO request) {
        Plant plant = plantRepository.findById(id)                       // 1. fetch existing
                .orElseThrow(() -> new NotFoundException("Plant not found: " + id));

        plant.setName(request.name());                                    // 2. modify its fields
        plant.setPhoto(request.photo());
        plant.setOriginalPrice(request.originalPrice());
        plant.setSellingPrice(request.sellingPrice());
        plant.setSortOrder(request.sortOrder());
        Grower grower = growerRepository.findById(request.growerId()).orElseThrow(() -> new NotFoundException("Grower not found: " + request.growerId()));
        plant.setGrower(grower);
        PlantType plantType = plantTypeRepository.findById(request.plantTypeId()).orElseThrow(() -> new NotFoundException("PlantType not found: " + request.plantTypeId()));
        plant.setPlantType(plantType);

        Plant saved = plantRepository.save(plant);                      // 3. save (runs an UPDATE)
        return toDto(saved);
    }

    @Override
    public void deletePlant(Long id) {
        if (!plantRepository.existsById(id)) {                            // 404 if it doesn't exist
            throw new NotFoundException("Plant not found: " + id);
        }
        plantRepository.deleteById(id);
    }

    // private helper: entity → DTO (we'll automate this with MapStruct later)
    private PlantDTO toDto(Plant p) {
        return new PlantDTO(p.getId(), p.getName(), p.getPhoto(), p.getOriginalPrice(), p.getSellingPrice() , p.getSortOrder(), p.getGrower().getName(), p.getPlantType().getName());
    }
}