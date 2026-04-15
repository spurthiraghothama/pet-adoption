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

import com.petadoption.dto.AppointmentDTO;
import com.petadoption.model.Appointment;
import com.petadoption.service.AppointmentService;
import com.petadoption.service.AppointmentServiceImpl;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<Appointment> book(@RequestBody AppointmentDTO appointmentDTO) {
        return new ResponseEntity<>(appointmentService.bookAppointment(appointmentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAll() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

@PostMapping("/{id}/confirm")
public ResponseEntity<Appointment> confirm(@PathVariable Long id) {
    return ResponseEntity.ok(((AppointmentServiceImpl) appointmentService).confirmAppointment(id));
}

@PostMapping("/{id}/complete")
public ResponseEntity<Appointment> complete(@PathVariable Long id) {
    return ResponseEntity.ok(((AppointmentServiceImpl) appointmentService).completeAppointment(id));
}

}