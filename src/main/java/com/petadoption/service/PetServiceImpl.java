package com.petadoption.service;

import com.petadoption.dto.PetDTO;
import com.petadoption.exception.ResourceNotFoundException;
import com.petadoption.model.Pet;
import com.petadoption.repository.PetRepository;
import com.petadoption.singleton.PetRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Override
    public Pet addPet(PetDTO petDTO) {
        Pet pet = Pet.builder()
                .name(petDTO.getName())
                .species(petDTO.getSpecies())
                .age(petDTO.getAge())
                .vaccinationStatus(petDTO.isVaccinationStatus())
                .availabilityStatus(petDTO.getAvailabilityStatus())
                .build();
        
        Pet savedPet = petRepository.save(pet);
        PetRegistry.getInstance().registerPet(savedPet);
        return savedPet;
    }

    @Override
    public List<Pet> getAvailablePets() {
        return petRepository.findByAvailabilityStatus("AVAILABLE");
    }

    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Override
    public void updateVaccinationStatus(Long petId, boolean status) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));
        pet.setVaccinationStatus(status);
        petRepository.save(pet);
    }
}
