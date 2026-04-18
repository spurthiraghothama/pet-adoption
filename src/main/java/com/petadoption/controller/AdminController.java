package com.petadoption.controller;

import com.petadoption.service.PetService;
import com.petadoption.service.UserService;
import com.petadoption.service.AppointmentService;
import com.petadoption.service.QueryService;
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

    @Autowired private PetService petService;
    @Autowired private UserService userService;
    @Autowired private AppointmentService appointmentService;
    @Autowired private QueryService queryService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllData() {
        Map<String, Object> data = new HashMap<>();
        data.put("pets", petService.getAllPets());
        data.put("users", userService.getAllUsers());
        data.put("appointments", appointmentService.getAllAppointments());
        data.put("queries", queryService.getAllQueries());
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/pets/all")
    public ResponseEntity<?> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping("/appointments/all")
    public ResponseEntity<?> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
}
