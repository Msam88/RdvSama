package com.example.appointmentsystem.service;

import com.example.appointmentsystem.model.Appointment;
import com.example.appointmentsystem.model.TimeSlot;
import com.example.appointmentsystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private TimeSlotService timeSlotService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PdfService pdfService;
    
    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        // Réserver le créneau
        TimeSlot slot = timeSlotService.reserveSlot(appointment.getTimeSlot().getId());
        appointment.setTimeSlot(slot);
        
        // Générer un numéro de confirmation unique
        appointment.setConfirmationNumber(generateConfirmationNumber());
        
        // Sauvegarder le rendez-vous
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        try {
            // Générer et envoyer la confirmation par email
            emailService.sendAppointmentConfirmation(savedAppointment);
        } catch (Exception e) {
            // Logger l'erreur mais ne pas empêcher la création du rendez-vous
            e.printStackTrace();
        }
        
        return savedAppointment;
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public List<Appointment> getWaitingAppointments() {
        return appointmentRepository.findByStatus("WAITING");
    }
    
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }
    
    public List<Appointment> getAppointmentsByUserId(Long userId) {
        return appointmentRepository.findByUserId(userId);
    }
    
    @Transactional
    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment appointment = getAppointmentById(id);
        
        if (appointmentDetails.getStatus() != null) {
            appointment.setStatus(appointmentDetails.getStatus());
            
            if ("CONFIRMED".equals(appointmentDetails.getStatus())) {
                try {
                    emailService.sendAppointmentConfirmation(appointment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return appointmentRepository.save(appointment);
    }
    
    @Transactional
    public void deleteAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        
        // Libérer le créneau
        TimeSlot slot = appointment.getTimeSlot();
        slot.setCurrentParticipants(slot.getCurrentParticipants() - 1);
        
        appointmentRepository.deleteById(id);
    }
    
    private String generateConfirmationNumber() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}