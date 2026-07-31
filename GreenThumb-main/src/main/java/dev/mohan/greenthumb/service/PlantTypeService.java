package dev.mohan.greenthumb.service;

import java.util.List;

import dev.mohan.greenthumb.dto.PlantTypeDTO;
import dev.mohan.greenthumb.dto.PlantTypeRequestDTO;

public interface PlantTypeService {
    List<PlantTypeDTO> getAllPlantTypes();
    PlantTypeDTO getPlantTypeById(Long id);
    PlantTypeDTO createPlantType(PlantTypeRequestDTO request);
    PlantTypeDTO updatePlantType(Long id, PlantTypeRequestDTO request);
    void deletePlantType(Long id);
}