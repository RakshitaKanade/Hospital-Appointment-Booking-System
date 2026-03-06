package com.hospital.appointment_system.controller;

import com.hospital.appointment_system.entity.Patient;
import com.hospital.appointment_system.repository.PatientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // ✅ CREATE
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    // ✅ GET ALL
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        return patientRepository.findById(id).map(patient -> {
            patient.setName(updatedPatient.getName());
            patient.setEmail(updatedPatient.getEmail());
            patient.setPhone(updatedPatient.getPhone());
            return patientRepository.save(patient);
        }).orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientRepository.deleteById(id);
    }
}