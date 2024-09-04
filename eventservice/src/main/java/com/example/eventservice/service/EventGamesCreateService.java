package com.example.eventservice.service;

import com.example.eventservice.model.event.entity.EventGamesEntity;

public interface EventGamesCreateService {
    EventGamesEntity createEventGames(final Long eventId, final Long gameId);
}
