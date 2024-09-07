package com.example.eventservice.service.impl;

import com.example.eventservice.repository.ItemRepository;
import com.example.eventservice.service.ItemDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemDeleteServiceImpl implements ItemDeleteService {
    private final ItemRepository itemRepository;
    @Override
    public void deleteItemsOfEvent(Long eventId) {
        itemRepository.deleteByEventId(eventId);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
