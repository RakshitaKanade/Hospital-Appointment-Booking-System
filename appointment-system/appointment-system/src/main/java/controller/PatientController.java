package com.hospital.appointment_system.controller;

import com.hospital.appointment_system.entity.Patient;
import com.hospital.appointment_system.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        if (patient.getName() == null || patient.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Patient name is required");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(patientRepository.save(patient));
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        return ResponseEntity.ok(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        patient.setName(updatedPatient.getName());
        patient.setEmail(updatedPatient.getEmail());
        patient.setPhone(updatedPatient.getPhone());
        return ResponseEntity.ok(patientRepository.save(patient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        if (!patientRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
        patientRepository.deleteById(id);
        return ResponseEntity.ok("Patient deleted successfully");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong: " + ex.getMessage());
    }
}