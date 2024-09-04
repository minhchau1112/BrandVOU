package com.example.eventservice.service;

import com.example.eventservice.model.item.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface ItemReadService {
    ItemEntity getItemById(final Long itemId);
    Page<ItemEntity> getItemsByEventId(final Long eventId, Pageable pageable);
    Page<ItemEntity> findByBrandId(Long brandId, String searchTerm, Pageable pageable);
}
