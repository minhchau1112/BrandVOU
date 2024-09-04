package com.example.eventservice.controller;

import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.event.entity.GameEntity;
import com.example.eventservice.service.GameReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/events/games")
@RequiredArgsConstructor
@Validated
public class GameController {
    private final GameReadService gameReadService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<List<GameEntity>> getAllGames() {
        List<GameEntity> gameEntityList = gameReadService.getAllGames();

        return ResponseEntity.ok(gameEntityList);
    }
}
