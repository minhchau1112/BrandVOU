package com.example.eventservice.service.impl;

import com.example.eventservice.client.UserServiceClient;
import com.example.eventservice.exception.EventAlreadyExistException;
import com.example.eventservice.model.event.dto.request.EventCreateRequest;
import com.example.eventservice.model.event.entity.BrandEntity;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.event.mapper.EventCreateRequestToEventEntityMapper;
import com.example.eventservice.repository.EventRepository;
import com.example.eventservice.service.EventCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCreateServiceImpl implements EventCreateService {
    private final EventRepository eventRepository;
    private final EventCreateRequestToEventEntityMapper eventCreateRequestToEventEntityMapper = EventCreateRequestToEventEntityMapper.initialize();
    private final UserServiceClient userServiceClient;
    @Override
    public EventEntity createEventForBrand(EventCreateRequest eventCreateRequest) {
        checkUniquenessEventName(eventCreateRequest.getName(), eventCreateRequest.getBrandId());

        // Extract the token from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();

        final BrandEntity brandEntity = userServiceClient.findBrandById(eventCreateRequest.getBrandId(), "Bearer " + token);

        final EventEntity eventEntity = eventCreateRequestToEventEntityMapper.mapForSaving(eventCreateRequest, brandEntity);
        EventEntity savedEventEntity = eventRepository.save(eventEntity);

        return savedEventEntity;
    }

    private void checkUniquenessEventName(final String eventName, final Long brandId) {
        if (eventRepository.existsByNameAndBrandId(eventName,  brandId)) {
            throw new EventAlreadyExistException("There is another event with given name: " + eventName);
        }
    }
}
