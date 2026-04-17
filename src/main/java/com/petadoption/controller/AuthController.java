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

        System.out.println("\n--- Login Attempt ---");
        System.out.println("Input Email: [" + email + "]");
        System.out.println("Input Role: [" + role + "]");

        // Find user by BOTH email and the role they are trying to log into
        Optional<User> userOpt = userRepository.findByEmailAndUserType(email, role);

        // Special handling for legacy role aliases (STAFF vs SHELTER_STAFF)
        if (userOpt.isEmpty()) {
            if (role.contains("STAFF")) {
                userOpt = userRepository.findByEmailAndUserType(email, "SHELTER_STAFF");
            } else if (role.equals("SHELTER_STAFF")) {
                userOpt = userRepository.findByEmailAndUserType(email, "STAFF");
            }
        }

        if (userOpt.isEmpty()) {
            System.out.println("Result: User NOT found in database for this role.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No account found for " + email + " as " + role);
        }

        User user = userOpt.get();
        System.out.println("User found: " + user.getName());
        System.out.println("Stored Password: [" + user.getPassword() + "]");
        System.out.println("Stored UserType: [" + user.getUserType() + "]");

        // Check password
        if (!user.getPassword().equals(password)) {
            System.out.println("Result: Password mismatch.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
        }

        // Check role
        String storedRole = user.getUserType().toUpperCase();
        boolean roleMatch = false;
        
        if (role.contains("STAFF")) {
            roleMatch = storedRole.contains("STAFF");
        } else if (role.contains("VET")) {
            roleMatch = storedRole.contains("VET");
        } else {
            roleMatch = storedRole.equals(role);
        }

        if (!roleMatch) {
            System.out.println("Result: Role mismatch. Expected " + role + " but found " + storedRole);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Role mismatch. You are registered as " + storedRole + " but tried logging in as " + role);
        }

        System.out.println("Result: SUCCESS");
        return ResponseEntity.ok(user);
    }
}
