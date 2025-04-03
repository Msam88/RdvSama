package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT t FROM TimeSlot t WHERE t.startTime BETWEEN ?1 AND ?2 AND t.currentParticipants < t.maxParticipants")
    List<TimeSlot> findAvailableSlots(LocalDateTime start, LocalDateTime end);
}