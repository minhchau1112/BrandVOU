package com.example.eventservice.service;

import com.example.eventservice.model.event.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventReadService {
    EventEntity getEventById(final Long eventId);
    Page<EventEntity> getEventsByBrandId(final Long brandId, String search, Pageable pageable);
    List<EventEntity> getAllEventsByBrandId(final Long brandId);
    List<EventEntity> findEventsOfBrandHaveTargetWord(Long brandId);
    Page<Object[]> findEventsWithNotificationStatus(Long playerId, String searchTerm, Pageable pageable);
}
