package com.example.eventservice.service.impl;

import com.example.eventservice.exception.EventAlreadyExistException;
import com.example.eventservice.exception.EventNotFoundException;
import com.example.eventservice.model.event.dto.request.EventUpdateRequest;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.event.mapper.EventUpdateRequestToEventEntityMapper;
import com.example.eventservice.repository.EventRepository;
import com.example.eventservice.service.EventUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventUpdateServiceImpl implements EventUpdateService {

    private final EventRepository eventRepository;
    private final EventUpdateRequestToEventEntityMapper eventUpdateRequestToEventEntityMapper = EventUpdateRequestToEventEntityMapper.initialize();

    @Override
    public EventEntity updateEventById(Long eventId, EventUpdateRequest eventUpdateRequest) {

        checkUniquenessEventName(eventUpdateRequest.getName(), eventUpdateRequest.getBrandId(), eventId);

        final EventEntity eventEntityToBeUpdate = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("With give eventID = " + eventId));

        eventUpdateRequestToEventEntityMapper.mapForUpdating(eventEntityToBeUpdate, eventUpdateRequest);

        EventEntity updatedEventEntity = eventRepository.save(eventEntityToBeUpdate);

        return updatedEventEntity;
    }

    private void checkUniquenessEventName(final String eventName, final Long brandId, final Long eventId) {
        EventEntity exitsEvent = eventRepository.findByNameAndBrandId(eventName, brandId);
        if (exitsEvent != null && !Objects.equals(exitsEvent.getId(), eventId)) {
            throw new EventAlreadyExistException("There is another event with given name: " + eventName);
        }
    }
}
