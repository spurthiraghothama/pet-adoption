package com.petadoption.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petadoption.dto.VetAppointmentDTO;
import com.petadoption.model.VetAppointment;
import com.petadoption.service.VetAppointmentService;
import com.petadoption.service.VetAppointmentServiceImpl;

@RestController
@RequestMapping("/vetappointments")
public class VetAppointmentController {

    @Autowired
    private VetAppointmentService vetAppointmentService;

    @PostMapping("/book")
    public ResponseEntity<VetAppointment> book(@RequestBody VetAppointmentDTO vetAppointmentDTO) {
        return new ResponseEntity<>(vetAppointmentService.bookAppointment(vetAppointmentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<VetAppointment>> getAll() {
        return ResponseEntity.ok(vetAppointmentService.getAllAppointments());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        vetAppointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VetAppointment> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(vetAppointmentService.updateStatus(id, status));
    }

@PostMapping("/{id}/confirm")
public ResponseEntity<VetAppointment> confirm(@PathVariable Long id) {
    return ResponseEntity.ok(((VetAppointmentServiceImpl) vetAppointmentService).confirmAppointment(id));
}

@PostMapping("/{id}/complete")
public ResponseEntity<VetAppointment> complete(@PathVariable Long id) {
    return ResponseEntity.ok(((VetAppointmentServiceImpl) vetAppointmentService).completeAppointment(id));
}

}