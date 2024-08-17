package com.example.brand_backend.controller;

import com.example.brand_backend.exception.ResourceNotFoundException;
import com.example.brand_backend.model.Events;
import com.example.brand_backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class EventController {
    @Autowired
    private EventRepository eventRepository;

    // get all events
    @GetMapping("/events")
    public List<Events> getAllEvents(){
        return eventRepository.findAll();
    }

    // create employee rest api
    @PostMapping("/events")
    public Events createEvent(@RequestBody Events event) {
        return eventRepository.save(event);
    }

    // get event by id rest api
    @GetMapping("/events/{id}")
    public ResponseEntity<Events> getEventById(@PathVariable int id) {
        Events event = eventRepository.findById((long) id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not exist with id :" + id));
        return ResponseEntity.ok(event);
    }

    // update event rest api
    @PutMapping("/events/{id}")
    public ResponseEntity<Events> updateEmployee(@PathVariable int id, @RequestBody Events eventDetails){
        Events event = eventRepository.findById((long) id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not exist with id :" + id));

        event.setName(eventDetails.getName());
        event.setImage(eventDetails.getImage());
        event.setVoucherCount(eventDetails.getVoucherCount());
        event.setStartTime(eventDetails.getStartTime());
        event.setEndTime(eventDetails.getEndTime());

        Events updatedEvent = eventRepository.save(event);
        return ResponseEntity.ok(updatedEvent);
    }

    // delete event rest api
    @DeleteMapping("/event/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEvent(@PathVariable int id){
        Events event = eventRepository.findById((long) id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not exist with id :" + id));

        eventRepository.delete(event);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
