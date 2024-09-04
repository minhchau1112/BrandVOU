package com.example.eventservice.service.impl;

import com.example.eventservice.repository.EventGamesRepository;
import com.example.eventservice.service.EventGamesDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventGamesDeleteServiceImpl implements EventGamesDeleteService {
    private final EventGamesRepository eventGamesRepository;
    @Override
    public void deleteEventsGamesByEventId(Long eventId) {
        eventGamesRepository.deleteAllByEventId(eventId);
    }
}
