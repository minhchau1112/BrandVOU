package com.example.eventservice.service;

import com.example.eventservice.model.item.dto.request.ItemCreateRequest;
import com.example.eventservice.model.item.entity.ItemEntity;

public interface ItemCreateService {
    ItemEntity createItemForShakeGame(final ItemCreateRequest itemCreateRequest);
}
