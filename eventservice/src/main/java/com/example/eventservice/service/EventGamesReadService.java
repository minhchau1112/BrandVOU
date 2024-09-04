package com.example.eventservice.service;

public interface EventGamesReadService {
    String findGameNamesByEventId(final Long eventId);
}
