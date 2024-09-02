package com.example.eventservice.controller;

import com.example.eventservice.model.event.dto.request.EventCreateRequest;
import com.example.eventservice.model.event.dto.request.EventUpdateRequest;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.service.EventCreateService;
import com.example.eventservice.service.EventReadService;
import com.example.eventservice.service.EventUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventCreateService eventCreateService;
    private final EventReadService eventReadService;
    private final EventUpdateService eventUpdateService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Long> createEventForBrand(@ModelAttribute final EventCreateRequest eventCreateRequest) {
        log.info("EventController | createEventForBrand");
        final EventEntity createdEvent = eventCreateService.createEventForBrand(eventCreateRequest);

        return ResponseEntity.ok(createdEvent.getId());
    }

    @GetMapping("/brand/{brandId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<Page<EventEntity>> getEventsByBrand(
            @PathVariable Long brandId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String searchTerm) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<EventEntity> eventsPage = eventReadService.getEventsByBrandId(brandId, searchTerm, pageable);

            return ResponseEntity.ok(eventsPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<EventEntity> getEventByEventId(@PathVariable Long eventId) {
        EventEntity event = eventReadService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<EventEntity> updateEvent(@PathVariable Long eventId, @ModelAttribute final EventUpdateRequest eventUpdateRequest) {
        EventEntity eventEntity = eventUpdateService.updateEventById(eventId, eventUpdateRequest);

        return ResponseEntity.ok(eventEntity);
    }

    @GetMapping("/brand/all/{brandId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<List<EventEntity>> getAllEventsByBrandId(@PathVariable Long brandId) {
        List<EventEntity> eventEntityList = eventReadService.getAllEventsByBrandId(brandId);

        return ResponseEntity.ok(eventEntityList);
    }
}
