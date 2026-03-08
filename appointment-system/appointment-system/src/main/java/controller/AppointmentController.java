package com.hospital.appointment_system.controller;

import com.hospital.appointment_system.entity.*;
import com.hospital.appointment_system.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentController(AppointmentRepository appointmentRepository,
                                 PatientRepository patientRepository,
                                 DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    // ✅ CREATE APPOINTMENT
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {

        // Null check for patient
        if (appointment.getPatient() == null || appointment.getPatient().getId() == null) {
            return ResponseEntity.badRequest().body("Patient ID is required");
        }

        // Null check for doctor
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            return ResponseEntity.badRequest().body("Doctor ID is required");
        }

        Patient patient = patientRepository.findById(appointment.getPatient().getId())
                .orElse(null);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }

        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElse(null);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        Appointment saved = appointmentRepository.save(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ GET ALL APPOINTMENTS
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return ResponseEntity.ok(appointments);
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
        }
        return ResponseEntity.ok(appointment);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        if (!appointmentRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
        }
        appointmentRepository.deleteById(id);
        return ResponseEntity.ok("Appointment deleted successfully");
    }

    // ✅ GLOBAL EXCEPTION HANDLER (catches any unexpected errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong: " + ex.getMessage());
    }
}