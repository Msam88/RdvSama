package com.example.appointmentsystem.service;

import com.example.appointmentsystem.model.TimeSlot;
import com.example.appointmentsystem.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TimeSlotService {
    
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    public List<TimeSlot> generateTimeSlotsForDay(LocalDateTime date) {
        LocalTime startTime = LocalTime.of(9, 0); // Début à 9h
        LocalTime endTime = LocalTime.of(17, 0);  // Fin à 17h
        
        while (startTime.plusMinutes(30).isBefore(endTime)) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(date.with(startTime));
            slot.setEndTime(date.with(startTime.plusMinutes(30)));
            timeSlotRepository.save(slot);
            
            startTime = startTime.plusMinutes(30);
        }
        
        return timeSlotRepository.findByStartTimeBetween(
            date.with(LocalTime.MIN),
            date.with(LocalTime.MAX)
        );
    }
    
    public List<TimeSlot> getAvailableTimeSlots(LocalDateTime date) {
        return timeSlotRepository.findAvailableSlots(
            date.with(LocalTime.MIN),
            date.with(LocalTime.MAX)
        );
    }
    
    public TimeSlot reserveSlot(Long slotId) {
        TimeSlot slot = timeSlotRepository.findById(slotId)
            .orElseThrow(() -> new RuntimeException("TimeSlot not found"));
            
        if (!slot.hasAvailableSpace()) {
            throw new RuntimeException("No available space in this time slot");
        }
        
        slot.setCurrentParticipants(slot.getCurrentParticipants() + 1);
        return timeSlotRepository.save(slot);
    }
}