package dev.mohan.greenthumb.service;

import java.util.List;

import dev.mohan.greenthumb.dto.PlantDTO;
import dev.mohan.greenthumb.dto.PlantRequestDTO;

public interface PlantService {
    List<PlantDTO> getAllPlants();
    PlantDTO getPlantById(Long id);
    PlantDTO createPlant(PlantRequestDTO request);
    PlantDTO updatePlant(Long id, PlantRequestDTO request);
    void deletePlant(Long id);
}