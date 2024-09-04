package com.example.eventservice.service;

import com.example.eventservice.model.item.dto.request.ItemUpdateRequest;
import com.example.eventservice.model.item.entity.ItemEntity;

public interface ItemUpdateService {
    ItemEntity updateItemById(final Long itemId, final ItemUpdateRequest itemUpdateRequest);
}
