package com.petadoption.service;

import com.petadoption.dto.PetDTO;
import com.petadoption.model.Pet;
import java.util.List;

public interface PetService {
    Pet addPet(PetDTO petDTO);
    List<Pet> getAvailablePets();
    List<Pet> getAllPets();
    void updateVaccinationStatus(Long petId, boolean status);
}
