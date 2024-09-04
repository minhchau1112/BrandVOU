package com.example.eventservice.service.impl;

import com.example.eventservice.repository.EventGamesRepository;
import com.example.eventservice.service.EventGamesReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventGamesReadServiceImpl implements EventGamesReadService {
    private final EventGamesRepository eventGamesRepository;
    @Override
    public String findGameNamesByEventId(Long eventId) {
        List<String> gamesNameList = eventGamesRepository.findGameNamesByEventId(eventId);
        String gamesName = String.join(";", gamesNameList);
        return gamesName;
    }
}
