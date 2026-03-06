package com.hospital.appointment_system.repository;

import com.hospital.appointment_system.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

} 