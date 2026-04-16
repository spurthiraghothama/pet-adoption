package com.petadoption.controller;

import com.petadoption.dto.AdoptionDTO;
import com.petadoption.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adoption")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @PostMapping("/create")
    public ResponseEntity<?> adopt(@RequestBody AdoptionDTO dto) {
        adoptionService.createAdoption(dto);
        return ResponseEntity.ok("Adoption successful");
    }

    @GetMapping("/my/{userId}")
    public ResponseEntity<?> getMyAdoptions(@PathVariable Long userId) {
        return ResponseEntity.ok(adoptionService.getAdoptionsByUser(userId));
    }
}