package com.petadoption.controller;

import com.petadoption.dto.UserDTO;
import com.petadoption.model.User;
import com.petadoption.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO) {
        try {
            return new ResponseEntity<>(userService.registerUser(userDTO), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
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
        return ResponseEntity.ok(userService.toggleVolunteerAvailability(id, status));
    }

    // Get all volunteers with availability status (for staff view)
    @GetMapping("/volunteers/status")
    public ResponseEntity<?> getVolunteersWithStatus() {
        return ResponseEntity.ok(userService.getAllVolunteers());
    }

    @GetMapping("/volunteers/{id}/status")
    public ResponseEntity<?> getVolunteer(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


}
