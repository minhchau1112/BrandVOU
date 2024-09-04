package com.example.eventservice.service.impl;

import com.example.eventservice.exception.ItemNotFoundException;
import com.example.eventservice.model.item.entity.ItemEntity;
import com.example.eventservice.repository.ItemRepository;
import com.example.eventservice.service.ItemReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemReadServiceImpl implements ItemReadService {
    private final ItemRepository itemRepository;
    @Override
    public ItemEntity getItemById(Long itemId) {
        Optional<ItemEntity> itemEntity = itemRepository.findById(itemId);

        if (itemEntity.isEmpty()) {
            throw new ItemNotFoundException("Couldn't find any Item");
        }

        return itemEntity.get();
    }

    @Override
    public Page<ItemEntity> getItemsByEventId(Long eventId, Pageable pageable) {
        return itemRepository.findByEventId(eventId, pageable);
    }

    @Override
    public Page<ItemEntity> findByBrandId(Long brandId, String searchTerm, Pageable pageable) {
        return itemRepository.findByBrandId(brandId, searchTerm, pageable);
    }
}
