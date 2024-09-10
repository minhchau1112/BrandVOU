package com.example.eventservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.eventservice.exception.EventAlreadyExistException;
import com.example.eventservice.exception.ItemAlreadyExistException;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.item.dto.request.ItemCreateRequest;
import com.example.eventservice.model.item.entity.ItemEntity;
import com.example.eventservice.model.item.mapper.ItemCreateRequestToItemEntityMapper;
import com.example.eventservice.repository.EventRepository;
import com.example.eventservice.repository.ItemRepository;
import com.example.eventservice.service.ItemCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemCreateServiceImpl implements ItemCreateService {
    private final ItemRepository itemRepository;
    private final Cloudinary cloudinary;
    private final ItemCreateRequestToItemEntityMapper itemCreateRequestToItemEntityMapper = ItemCreateRequestToItemEntityMapper.initialize();
    private final EventRepository eventRepository;
    @Override
    public ItemEntity createItemForShakeGame(ItemCreateRequest itemCreateRequest) {
        boolean isContinue = isUniquenessItemName(itemCreateRequest.getName(), itemCreateRequest.getEventId());

        if (isContinue) {
            Optional<EventEntity> event = eventRepository.findById(itemCreateRequest.getEventId());

            final ItemEntity itemEntity = itemCreateRequestToItemEntityMapper.mapForSaving(itemCreateRequest);
            itemEntity.setEvent(event.get());

            ItemEntity savedItemEntity = itemRepository.save(itemEntity);

            return savedItemEntity;
        }
        return null;
    }

    private boolean isUniquenessItemName(final String itemName, final Long eventId) {
        if (itemRepository.existsByNameAndEventId(itemName,  eventId)) {
            return false;
        }
        return true;
    }
}
