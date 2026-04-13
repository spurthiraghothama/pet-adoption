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
        Optional<User> user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(loginDTO.getEmail()) 
                        && u.getPassword().equals(loginDTO.getPassword()))
                .filter(u -> {
                    String storedRole = u.getUserType().toUpperCase(); // e.g., "SHELTER_STAFF"
                    String loginRole = loginDTO.getRole().toUpperCase(); // e.g., "STAFF"
                    
                    if (loginRole.contains("STAFF")) {
                        return storedRole.contains("STAFF");
                    }
                    return storedRole.equals(loginRole);
                })
                .findFirst();

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials or role");
        }
    }
}
