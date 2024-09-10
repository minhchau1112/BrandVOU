package com.example.eventservice.service.impl;

import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.repository.NotificationRepository;
import com.example.eventservice.service.NotificationReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationReadServiceImpl implements NotificationReadService {
    private final NotificationRepository notificationRepository;
    @Override
    public Page<EventEntity> findFavouriteEvent(Long playerId, String searchTerm, Pageable pageable) {
        return notificationRepository.findFavouriteEvent(playerId, searchTerm, pageable);
    }
}
