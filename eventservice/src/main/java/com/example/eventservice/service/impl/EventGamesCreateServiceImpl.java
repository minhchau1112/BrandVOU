package com.example.eventservice.service.impl;

import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.event.entity.EventGamesEntity;
import com.example.eventservice.model.event.entity.GameEntity;
import com.example.eventservice.repository.EventGamesRepository;
import com.example.eventservice.repository.EventRepository;
import com.example.eventservice.repository.GameRepository;
import com.example.eventservice.service.EventGamesCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventGamesCreateServiceImpl implements EventGamesCreateService {
    private final EventGamesRepository eventGamesRepository;
    private final EventRepository eventRepository;
    private final GameRepository gameRepository;
    @Override
    public EventGamesEntity createEventGames(final Long eventId, final Long gameId) {
        Optional<EventEntity> event = eventRepository.findById(eventId);
        Optional<GameEntity> game = gameRepository.findById(gameId);

        if (!event.isEmpty() && !game.isEmpty()) {
            log.info("createEventGames| success");
            EventGamesEntity eventGamesEntity = EventGamesEntity
                    .builder()
                    .game(game.get())
                    .event(event.get())
                    .build();

            EventGamesEntity savedEventGamesEntity = eventGamesRepository.save(eventGamesEntity);

            log.info("createEventGames| " + savedEventGamesEntity.getId());

            return savedEventGamesEntity;
        }
        log.info("createEventGames| null");

        return null;
    }
}
