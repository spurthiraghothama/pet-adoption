package com.petadoption.controller;

import com.petadoption.dto.UserDTO;
import com.petadoption.model.User;
import com.petadoption.model.Volunteer;
import com.petadoption.service.UserService;
import com.petadoption.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.registerUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<java.util.List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/volunteers")
    public ResponseEntity<java.util.List<User>> getVolunteers() {
        return ResponseEntity.ok(userService.getAllUsers().stream()
            .filter(u -> u.getUserType().equalsIgnoreCase("VOLUNTEER"))
            .toList());
    }

    // Toggle volunteer availability
    @PatchMapping("/volunteers/{id}/availability")
    public ResponseEntity<?> toggleAvailability(@PathVariable Long id, @RequestParam Boolean status) {
        Volunteer vol = (Volunteer) userRepository.findById(id).orElseThrow();
        vol.setAvailabilityStatus(status);
        userRepository.save(vol);
        return ResponseEntity.ok(vol);
    }

    // Get all volunteers with availability status (for staff view)
    @GetMapping("/volunteers/status")
    public ResponseEntity<?> getVolunteersWithStatus() {
        List<User> volunteers = userRepository.findByUserType("VOLUNTEER");
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/volunteers/{id}/status")
    public ResponseEntity<?> getVolunteer(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id).orElseThrow());
    }


}
