package com.example.eventservice.controller;

import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.service.EventGamesReadService;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/events/eventgames")
@RequiredArgsConstructor
@Validated
public class EventGamesController {
    private final EventGamesReadService eventGamesReadService;
    @GetMapping("/games-name")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public String findGameNamesByEventId(@RequestParam Long eventId) {
        log.info("EventGamesController| findGameNamesByEventId");
        String gamenames = eventGamesReadService.findGameNamesByEventId(eventId);
        log.info("gamenames| " + gamenames);
        return gamenames;
    }
}
