package com.petadoption.service;

import com.petadoption.dto.AdoptionDTO;
import com.petadoption.model.*;

import java.util.List;
import com.petadoption.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdoptionServiceImpl implements AdoptionService {

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createAdoption(AdoptionDTO dto) {

        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        User user = userRepository.findById(dto.getAdopterId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // prevent double adoption
        if (pet.getAvailabilityStatus() == Status.ADOPTED) {
            throw new RuntimeException("Pet already adopted");
        }

        // mark pet adopted
        pet.setAvailabilityStatus(Status.ADOPTED);
        petRepository.save(pet);

        Adoption adoption = new Adoption();
        adoption.setPet(pet);
        adoption.setAdopter(user);
        adoption.setAdoptionDate(LocalDateTime.now());
        adoption.setStatus("ACTIVE");

        adoptionRepository.save(adoption);
    }

    @Override
    public List<Adoption> getAdoptionsByUser(Long userId) {
        return adoptionRepository.findByAdopter_UserId(userId);
    }
}