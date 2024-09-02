package com.example.eventservice.service.impl;

import com.example.eventservice.exception.EventNotFoundException;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.repository.EventRepository;
import com.example.eventservice.service.EventReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventReadServiceImpl implements EventReadService {

    private final EventRepository eventRepository;

    @Override
    public EventEntity getEventById(Long eventId) {
        Optional<EventEntity> eventEntity = eventRepository.findById(eventId);
        if (eventEntity.isEmpty()) {
            throw new EventNotFoundException("Couldn't find any Event");
        }
        return eventEntity.get();
    }

    @Override
    public Page<EventEntity> getEventsByBrandId(Long brandId, String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return eventRepository.findByBrandId(brandId, pageable);
        } else {
            return eventRepository.findByBrandIdAndSearch(brandId, search, pageable);
        }
    }

    @Override
    public List<EventEntity> getAllEventsByBrandId(Long brandId) {
        return eventRepository.findAllByBrandId(brandId);
    }


}
