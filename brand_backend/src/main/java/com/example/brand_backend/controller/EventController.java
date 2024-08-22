package com.example.brand_backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.brand_backend.exception.ResourceNotFoundException;
import com.example.brand_backend.model.Brands;
import com.example.brand_backend.model.Events;
import com.example.brand_backend.repository.BrandRepository;
import com.example.brand_backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private Cloudinary cloudinary;
    @PostMapping("/{brandId}")
    public ResponseEntity<Events> createEvent(
            @PathVariable Long brandId,
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile image,
            @RequestParam("voucherCount") int voucherCount,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam("gameType") String gameType){

        Brands brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                imageUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        Events event = new Events();
        event.setName(name);
        event.setImage(imageUrl);
        event.setVoucherCount(voucherCount);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setBrand(brand);
        event.setGameType(gameType);

        Events savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<List<Events>> getEventsByBrand(@PathVariable Long brandId) {
        try {
            System.out.println("Brand ID: " + brandId);
            List<Events> events = eventRepository.findByBrandId(brandId);
            System.out.println("Found events: " + events.size());
            for(int i = 0; i < events.size(); i++) {
                System.out.println(events.get(i).getName());
            }
            if (events.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{brandId}")
    public Events updateEvent(@PathVariable Long id, @RequestBody Events eventDetails) {
        Events event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        event.setName(eventDetails.getName());
        event.setImage(eventDetails.getImage());
        event.setVoucherCount(eventDetails.getVoucherCount());
        event.setStartTime(eventDetails.getStartTime());
        event.setEndTime(eventDetails.getEndTime());
        return eventRepository.save(event);
    }
}
