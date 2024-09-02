package com.example.eventservice.service;

import com.example.eventservice.model.event.dto.request.EventCreateRequest;
import com.example.eventservice.model.event.entity.EventEntity;

public interface EventCreateService {
    EventEntity createEventForBrand(final EventCreateRequest eventCreateService);
}
