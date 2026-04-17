package com.petadoption.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petadoption.dto.VetAppointmentDTO;
import com.petadoption.exception.ResourceNotFoundException;
import com.petadoption.model.VetAppointment;
import com.petadoption.model.VetAppointmentStatus;
import com.petadoption.model.Pet;
import com.petadoption.model.User;
import com.petadoption.repository.VetAppointmentRepository;
import com.petadoption.repository.PetRepository;
import com.petadoption.repository.UserRepository;

@Service
public class VetAppointmentServiceImpl implements VetAppointmentService {

    @Autowired
    private VetAppointmentRepository vetAppointmentRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
public VetAppointment bookAppointment(VetAppointmentDTO appointmentDTO) {
    Pet pet = petRepository.findById(appointmentDTO.getPetId())
            .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

    User user = userRepository.findById(appointmentDTO.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    VetAppointment appointment = VetAppointment.builder()
            .date(appointmentDTO.getDate())
            .time(appointmentDTO.getTime())
            .status(VetAppointmentStatus.BOOKED)
            .appointmentType("VET_CHECK")
            .pet(pet)
            .user(user)
            .build();

    com.petadoption.util.AppointmentManager.getInstance().incrementAppointments();
    return vetAppointmentRepository.save(appointment);
}

public VetAppointment confirmAppointment(Long id) {
    VetAppointment app = vetAppointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

    app.setStatus(VetAppointmentStatus.CONFIRMED);
    return vetAppointmentRepository.save(app);
}

    @Override
    public void cancelAppointment(Long appointmentId) {
        VetAppointment app = vetAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        app.setStatus(VetAppointmentStatus.CANCELLED);
        vetAppointmentRepository.save(app);
    }

    @Override
    public java.util.List<VetAppointment> getAllAppointments() {
        return vetAppointmentRepository.findAll();
    }

    @Override
public VetAppointment updateStatus(Long id, String status) {
    VetAppointment app = vetAppointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

    try {
        app.setStatus(VetAppointmentStatus.valueOf(status.toUpperCase()));
    } catch (Exception e) {
        throw new RuntimeException("Invalid status value");
    }

    return vetAppointmentRepository.save(app);
}

    
    public VetAppointment completeAppointment(Long id) {
    VetAppointment app = vetAppointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

    app.setStatus(VetAppointmentStatus.COMPLETED);

    Pet pet = app.getPet();
    pet.setVaccinationStatus(true);
    petRepository.save(pet);

    return vetAppointmentRepository.save(app);
}
}