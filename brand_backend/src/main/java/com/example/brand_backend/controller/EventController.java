package com.example.brand_backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.brand_backend.exception.ResourceNotFoundException;
import com.example.brand_backend.model.Brands;
import com.example.brand_backend.model.Events;
import com.example.brand_backend.repository.BrandRepository;
import com.example.brand_backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<Page<Events>> getEventsByBrand(
            @PathVariable Long brandId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            System.out.println("Brand ID: " + brandId);
            Pageable pageable = PageRequest.of(page, size);
            Page<Events> eventsPage = eventRepository.findByBrandId(brandId, pageable);
            System.out.println("Found events: " + eventsPage.getNumberOfElements());
            for (Events event : eventsPage.getContent()) {
                System.out.println(event.getName());
            }
            if (eventsPage.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(eventsPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/view-detail/{eventId}")
    public ResponseEntity<Events> getEventByEventId(@PathVariable Long eventId) {
        Optional<Events> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            Events result = event.get();
            return ResponseEntity.ok(result);
        } else {
            throw new ResourceNotFoundException("Event not found with id " + eventId);
        }
    }
    @PutMapping("/update/{eventId}")
    public ResponseEntity<Events> updateEvent(
            @PathVariable Long eventId,
            @RequestParam("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("voucherCount") int voucherCount,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam("gameType") String gameType) {

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                imageUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        Events event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setName(name);
        if (imageUrl != null) {
            event.setImage(imageUrl);
        }
        event.setVoucherCount(voucherCount);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setGameType(gameType);

        Events savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }
    @GetMapping("/check-duplicate")
    public ResponseEntity<Boolean> checkDuplicateCode(
            @RequestParam String name,
            @RequestParam Long brandID) {
        boolean isDuplicate = eventRepository.existsByNameAndBrandId(name, brandID);
        return new ResponseEntity<>(isDuplicate, HttpStatus.OK);
    }
}
