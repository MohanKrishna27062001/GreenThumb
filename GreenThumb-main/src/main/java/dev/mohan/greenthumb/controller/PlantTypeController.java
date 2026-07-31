package dev.mohan.greenthumb.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.mohan.greenthumb.dto.PlantTypeDTO;
import dev.mohan.greenthumb.dto.PlantTypeRequestDTO;
import dev.mohan.greenthumb.service.PlantTypeService;
import jakarta.validation.Valid;

@RestController                       // "this class handles HTTP and returns JSON"
@RequestMapping("/api/plant-types")      // base URL for every method here
public class PlantTypeController {

    private final PlantTypeService plantTypeService;

    public PlantTypeController(PlantTypeService plantTypeService) {
        this.plantTypeService = plantTypeService;
    }

    @GetMapping                       // GET /api/plant-types
    public List<PlantTypeDTO> getAll() {
        return plantTypeService.getAllPlantTypes();
    }

    @GetMapping("/{id}")              // GET /api/plant-types/5
    public PlantTypeDTO getOne(@PathVariable Long id) {
        return plantTypeService.getPlantTypeById(id);
    }

    @PostMapping                                  // handles HTTP POST /api/plant-types
    public ResponseEntity<PlantTypeDTO> create(@Valid @RequestBody PlantTypeRequestDTO request) {
        PlantTypeDTO created = plantTypeService.createPlantType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);   // 201
    }

    @PutMapping("/{id}")                      // PUT /api/plant-types/5
    public PlantTypeDTO update(@PathVariable Long id, @Valid @RequestBody PlantTypeRequestDTO request) {
        return plantTypeService.updatePlantType(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")                   // DELETE /api/plant-types/5
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        plantTypeService.deletePlantType(id);
        return ResponseEntity.noContent().build();   // 204 No Content
    }
}