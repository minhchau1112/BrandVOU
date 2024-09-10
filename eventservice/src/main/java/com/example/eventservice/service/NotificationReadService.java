package com.example.eventservice.service;

import com.example.eventservice.model.event.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationReadService {
    Page<EventEntity> findFavouriteEvent(Long playerId, String searchTerm, Pageable pageable);

}
