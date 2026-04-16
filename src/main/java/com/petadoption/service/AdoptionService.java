package com.petadoption.service;

import com.petadoption.dto.AdoptionDTO;
import com.petadoption.model.Adoption;

import java.util.List;

public interface AdoptionService {
    void createAdoption(AdoptionDTO dto);
    List<Adoption> getAdoptionsByUser(Long userId);
}