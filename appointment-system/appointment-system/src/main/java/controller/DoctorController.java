package com.hospital.appointment_system.controller;

import com.hospital.appointment_system.entity.Doctor;
import com.hospital.appointment_system.repository.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        if (doctor.getName() == null || doctor.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Doctor name is required");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorRepository.save(doctor));
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        doctor.setName(updatedDoctor.getName());
        doctor.setSpecialization(updatedDoctor.getSpecialization());
        doctor.setEmail(updatedDoctor.getEmail());
        return ResponseEntity.ok(doctorRepository.save(doctor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        if (!doctorRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        doctorRepository.deleteById(id);
        return ResponseEntity.ok("Doctor deleted successfully");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong: " + ex.getMessage());
    }
}