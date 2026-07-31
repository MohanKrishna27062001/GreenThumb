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

import dev.mohan.greenthumb.dto.GrowerDTO;
import dev.mohan.greenthumb.dto.GrowerRequestDTO;
import dev.mohan.greenthumb.service.GrowerService;
import jakarta.validation.Valid;

@RestController                       // "this class handles HTTP and returns JSON"
@RequestMapping("/api/growers")      // base URL for every method here
public class GrowerController {

    private final GrowerService growerService;

    public GrowerController(GrowerService growerService) {
        this.growerService = growerService;
    }

    @GetMapping                       // GET /api/growers
    public List<GrowerDTO> getAll() {
        return growerService.getAllGrowers();
    }

    @GetMapping("/{id}")              // GET /api/growers/5
    public GrowerDTO getOne(@PathVariable Long id) {
        return growerService.getGrowerById(id);
    }

    @PostMapping                                  // handles HTTP POST /api/growers
    public ResponseEntity<GrowerDTO> create(@Valid @RequestBody GrowerRequestDTO request) {
        GrowerDTO created = growerService.createGrower(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);   // 201
    }

    @PutMapping("/{id}")                      // PUT /api/growers/5
    public GrowerDTO update(@PathVariable Long id, @Valid @RequestBody GrowerRequestDTO request) {
        return growerService.updateGrower(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")                   // DELETE /api/growers/5
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        growerService.deleteGrower(id);
        return ResponseEntity.noContent().build();   // 204 No Content
    }
}