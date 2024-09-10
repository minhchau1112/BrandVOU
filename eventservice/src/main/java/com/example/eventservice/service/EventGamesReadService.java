package com.example.eventservice.service;

import com.example.eventservice.model.event.entity.EventGamesEntity;

import java.util.List;

public interface EventGamesReadService {
    String findGameNamesByEventId(final Long eventId);
    List<EventGamesEntity> findEventGamesEntityByEventIdAndType(Long eventId, String type);
}
