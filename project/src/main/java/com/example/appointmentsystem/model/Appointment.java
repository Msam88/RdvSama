package com.example.appointmentsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;
    
    private LocalDateTime createdAt;
    private String status; // WAITING, CONFIRMED, CANCELLED
    private String confirmationNumber;
    private Boolean emailSent = false;
    private Boolean pdfGenerated = false;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}