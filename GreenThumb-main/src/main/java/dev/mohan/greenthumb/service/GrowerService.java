package dev.mohan.greenthumb.service;

import java.util.List;

import dev.mohan.greenthumb.dto.GrowerDTO;
import dev.mohan.greenthumb.dto.GrowerRequestDTO;

public interface GrowerService {
    List<GrowerDTO> getAllGrowers();
    GrowerDTO getGrowerById(Long id);
    GrowerDTO createGrower(GrowerRequestDTO request);
    GrowerDTO updateGrower(Long id, GrowerRequestDTO request);
    void deleteGrower(Long id);
}