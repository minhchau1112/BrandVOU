package com.example.eventservice.service;

import com.example.eventservice.model.event.entity.GameEntity;

import java.util.List;

public interface GameReadService {
    List<GameEntity> getAllGames();
}
