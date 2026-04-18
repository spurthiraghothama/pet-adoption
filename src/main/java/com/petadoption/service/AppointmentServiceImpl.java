package com.petadoption.service;

import com.petadoption.dto.AppointmentDTO;
import com.petadoption.exception.ResourceNotFoundException;
import com.petadoption.model.Appointment;
import com.petadoption.model.Pet;
import com.petadoption.model.User;
import com.petadoption.model.VetAppointment;
import com.petadoption.model.VetAppointmentStatus;
import com.petadoption.repository.AppointmentRepository;
import com.petadoption.repository.PetRepository;
import com.petadoption.repository.UserRepository;
import com.petadoption.repository.VetAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VetAppointmentRepository vetAppointmentRepository;

    @Override
    public Appointment bookAppointment(AppointmentDTO appointmentDTO) {
        com.petadoption.util.AppointmentScheduleValidator.validate(
                appointmentDTO.getDate(),
                appointmentDTO.getTime()
        );

        validateSlotAvailability(appointmentDTO);

        Pet pet = petRepository.findById(appointmentDTO.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        User user = userRepository.findById(appointmentDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Appointment appointment = Appointment.builder()
                .date(appointmentDTO.getDate())
                .time(appointmentDTO.getTime())
                .status("PENDING")
                .appointmentType(appointmentDTO.getAppointmentType())
                .pet(pet)
                .user(user)
                .build();
        
        com.petadoption.util.AppointmentManager.getInstance().incrementAppointments();
        return appointmentRepository.save(appointment);
    }

    private void validateSlotAvailability(AppointmentDTO appointmentDTO) {
        String appointmentType = appointmentDTO.getAppointmentType() == null
                ? "VISIT"
                : appointmentDTO.getAppointmentType().toUpperCase();

        List<Appointment> sameTimeAppointments = appointmentRepository.findAll().stream()
                .filter(this::isActiveAppointment)
                .filter(a -> isSameSlot(a.getDate(), a.getTime(), appointmentDTO.getDate(), appointmentDTO.getTime()))
                .toList();

        if ("VET_CHECK".equals(appointmentType)) {
            boolean hasVetConflictInAppointments = sameTimeAppointments.stream()
                    .anyMatch(a -> "VET_CHECK".equalsIgnoreCase(a.getAppointmentType()));

            boolean hasVetConflictInVetAppointments = vetAppointmentRepository
                    .findAll()
                    .stream()
                    .filter(this::isActiveVetAppointment)
                    .filter(a -> isSameSlot(a.getDate(), a.getTime(), appointmentDTO.getDate(), appointmentDTO.getTime()))
                    .anyMatch(this::isActiveVetAppointment);

            if (hasVetConflictInAppointments || hasVetConflictInVetAppointments) {
                throw new RuntimeException("Slot not avaliable try again!");
            }
            return;
        }

        boolean hasVisitConflict = sameTimeAppointments.stream()
                .anyMatch(a -> !"VET_CHECK".equalsIgnoreCase(a.getAppointmentType()));

        if (hasVisitConflict) {
            throw new RuntimeException("Slot not avaliable try again!");
        }
    }

    private boolean isActiveAppointment(Appointment appointment) {
        String status = appointment.getStatus();
        return status == null || (
                !"REJECTED".equalsIgnoreCase(status) &&
                !"COMPLETED".equalsIgnoreCase(status) &&
                !"EXPIRED".equalsIgnoreCase(status)
        );
    }

    private boolean isActiveVetAppointment(VetAppointment appointment) {
        VetAppointmentStatus status = appointment.getStatus();
        return status != VetAppointmentStatus.REJECTED &&
                status != VetAppointmentStatus.COMPLETED &&
                status != VetAppointmentStatus.CANCELLED;
    }

    private boolean isSameSlot(java.time.LocalDate existingDate, LocalTime existingTime,
                               java.time.LocalDate requestedDate, LocalTime requestedTime) {
        if (existingDate == null || existingTime == null || requestedDate == null || requestedTime == null) {
            return false;
        }

        return existingDate.equals(requestedDate) &&
                existingTime.truncatedTo(ChronoUnit.MINUTES)
                        .equals(requestedTime.truncatedTo(ChronoUnit.MINUTES));
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        if (appointmentRepository.existsById(appointmentId)) {
            appointmentRepository.deleteById(appointmentId);
            com.petadoption.util.AppointmentManager.getInstance().decrementAppointments();
        }
    }

    @Override
    public java.util.List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment updateStatus(Long id, String status) {
        Appointment app = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        app.setStatus(status);
        return appointmentRepository.save(app);
    }

    @Override
    public Appointment markCompleted(Long id) {
        Appointment app = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        app.setStatus("COMPLETED");
        return appointmentRepository.save(app);
    }

    @Override
    public Appointment markExpired(Long id) {
        Appointment app = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        app.setStatus("EXPIRED");
        return appointmentRepository.save(app);
    }

}
