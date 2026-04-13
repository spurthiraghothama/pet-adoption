package com.petadoption.controller;

import com.petadoption.model.*;
import com.petadoption.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/data")
public class AdminController {

    @Autowired private PetRepository petRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private QueryRepository queryRepository;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllData() {
        Map<String, Object> data = new HashMap<>();
        data.put("pets", petRepository.findAll());
        data.put("users", userRepository.findAll());
        data.put("appointments", appointmentRepository.findAll());
        data.put("queries", queryRepository.findAll());
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/pets/all")
    public ResponseEntity<?> getAllPets() {
        return ResponseEntity.ok(petRepository.findAll());
    }

    @GetMapping("/appointments/all")
    public ResponseEntity<?> getAllAppointments() {
        return ResponseEntity.ok(appointmentRepository.findAll());
    }
}
