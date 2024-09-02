package com.example.eventservice.service;

import com.example.eventservice.model.event.dto.request.EventUpdateRequest;
import com.example.eventservice.model.event.entity.EventEntity;

public interface EventUpdateService {
    EventEntity updateEventById(final Long eventId, final EventUpdateRequest eventUpdateRequest);
}
