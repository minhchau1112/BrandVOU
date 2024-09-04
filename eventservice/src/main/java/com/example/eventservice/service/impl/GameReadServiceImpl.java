package com.example.eventservice.service.impl;

import com.example.eventservice.model.event.entity.GameEntity;
import com.example.eventservice.repository.GameRepository;
import com.example.eventservice.service.GameReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameReadServiceImpl implements GameReadService {
    private final GameRepository gameRepository;
    @Override
    public List<GameEntity> getAllGames() {
        List<GameEntity> games = gameRepository.findAll();
        return games;
    }
}
