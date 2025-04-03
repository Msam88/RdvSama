package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.model.TimeSlot;
import com.example.appointmentsystem.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotController {
    
    @Autowired
    private TimeSlotService timeSlotService;
    
    @GetMapping("/generate")
    public ResponseEntity<List<TimeSlot>> generateTimeSlotsForDay(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ) {
        return ResponseEntity.ok(timeSlotService.generateTimeSlotsForDay(date));
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ) {
        return ResponseEntity.ok(timeSlotService.getAvailableTimeSlots(date));
    }
}