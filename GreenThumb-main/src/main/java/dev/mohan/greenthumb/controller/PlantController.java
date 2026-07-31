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

import dev.mohan.greenthumb.dto.PlantDTO;
import dev.mohan.greenthumb.dto.PlantRequestDTO;
import dev.mohan.greenthumb.service.PlantService;
import jakarta.validation.Valid;

@RestController                       // "this class handles HTTP and returns JSON"
@RequestMapping("/api/plants")      // base URL for every method here
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping                       // GET /api/plants
    public List<PlantDTO> getAll() {
        return plantService.getAllPlants();
    }

    @GetMapping("/{id}")              // GET /api/plants/1
    public PlantDTO getOne(@PathVariable Long id) {
        return plantService.getPlantById(id);
    }

    @PostMapping                                  // handles HTTP POST /api/plants
    public ResponseEntity<PlantDTO> create(@Valid @RequestBody PlantRequestDTO request) {
        PlantDTO created = plantService.createPlant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);   // 201
    }

    @PutMapping("/{id}")                      // PUT /api/plants/5
    public PlantDTO update(@PathVariable Long id, @Valid @RequestBody PlantRequestDTO request) {
        return plantService.updatePlant(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")                   // DELETE /api/plants/5
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();   // 204 No Content
    }
}