package com.example.appointmentsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxParticipants = 10;
    private Integer currentParticipants = 0;
    
    @OneToMany(mappedBy = "timeSlot")
    private Set<Appointment> appointments;
    
    public boolean hasAvailableSpace() {
        return currentParticipants < maxParticipants;
    }
}