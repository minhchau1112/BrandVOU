package com.example.eventservice.service;

import com.example.eventservice.model.event.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventReadService {
    EventEntity getEventById(final Long eventId);
    Page<EventEntity> getEventsByBrandId(final Long brandId, Pageable pageable);
}
