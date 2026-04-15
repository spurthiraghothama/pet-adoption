package com.petadoption.controller;

import com.petadoption.dto.LoginDTO;
import com.petadoption.model.User;
import com.petadoption.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String email = loginDTO.getEmail() != null ? loginDTO.getEmail().trim() : "";
        String password = loginDTO.getPassword() != null ? loginDTO.getPassword().trim() : "";
        String role = loginDTO.getRole() != null ? loginDTO.getRole().trim().toUpperCase() : "";

        System.out.println("Login attempt: Email=" + email + ", Role=" + role);

        Optional<User> user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password))
                .filter(u -> {
                    String storedRole = u.getUserType().toUpperCase();
                    if (role.contains("STAFF")) return storedRole.contains("STAFF");
                    if (role.contains("VET")) return storedRole.contains("VET");
                    return storedRole.equals(role);
                })
                .findFirst();

        if (user.isPresent()) {
            System.out.println("Login success for: " + email);
            return ResponseEntity.ok(user.get());
        } else {
            System.out.println("Login failed for: " + email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials or role mismatch.");
        }
    }
}
