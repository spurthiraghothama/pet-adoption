package com.petadoption.service;

import com.petadoption.dto.PetDTO;
import com.petadoption.exception.ResourceNotFoundException;
import com.petadoption.model.Pet;
import com.petadoption.repository.PetRepository;
import com.petadoption.singleton.PetRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.petadoption.model.Status;


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
                .ageMonths(petDTO.getAgeMonths())
                .vaccinationStatus(petDTO.isVaccinationStatus())
                .availabilityStatus(petDTO.getAvailabilityStatus() != null ? petDTO.getAvailabilityStatus() : Status.REGISTERED)
                .imageUrl(petDTO.getImageUrl() != null ? petDTO.getImageUrl() : "img/pet.png")
                .healthStatus(petDTO.getHealthStatus() != null ? petDTO.getHealthStatus() : "Healthy")
                .registeredById(petDTO.getRegisteredById())
                .registeredByType(petDTO.getRegisteredByType())
                .build();
        
        Pet savedPet = petRepository.save(pet);
        PetRegistry.getInstance().registerPet(savedPet);
        return savedPet;
    }

    @Override
    public List<Pet> getAvailablePets() {
        return petRepository.findByAvailabilityStatus(Status.AVAILABLE);
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

    @Override
    public Pet approvePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found: " + petId));
        pet.setAvailabilityStatus(Status.AVAILABLE);
        return petRepository.save(pet);
    }

    @Override
    public Pet rejectPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found: " + petId));
        pet.setAvailabilityStatus(Status.REJECTED);
        return petRepository.save(pet);
    }

    @Override
    public List<Pet> getPetsByRegistrant(Long userId, String userType) {
        return petRepository.findByRegisteredByIdAndRegisteredByType(userId, userType);
    }

    @Override
    public List<Pet> getPendingReviewPets() {
        return petRepository.findByAvailabilityStatusIn(
            List.of(Status.REGISTERED, Status.UNDER_REVIEW)
        );
    }

}
