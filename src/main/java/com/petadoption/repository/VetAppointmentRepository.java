package com.petadoption.repository;

import com.petadoption.model.VetAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VetAppointmentRepository extends JpaRepository<VetAppointment, Long> {
    
}

