package com.petadoption.controller;

import com.petadoption.dto.PetDTO;
import com.petadoption.model.Pet;
import com.petadoption.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<Pet>> getAllAvailablePets() {
        return ResponseEntity.ok(petService.getAvailablePets());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Pet>> getAll() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @PostMapping("/add")
    public ResponseEntity<Pet> addPet(@RequestBody PetDTO petDTO) {
        return new ResponseEntity<>(petService.addPet(petDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/vaccination")
    public ResponseEntity<Void> updateVaccination(@PathVariable Long id, @RequestParam boolean status) {
        petService.updateVaccinationStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending-review")
    public ResponseEntity<List<Pet>> getPendingReview() {
        return ResponseEntity.ok(petService.getPendingReviewPets());
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<Pet> approvePet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.approvePet(id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<Pet> rejectPet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.rejectPet(id));
    }

    @GetMapping("/my-registrations")
    public ResponseEntity<List<Pet>> getMyRegistrations(
            @RequestParam Long userId,
            @RequestParam String userType) {
        return ResponseEntity.ok(petService.getPetsByRegistrant(userId, userType));
    }

}
