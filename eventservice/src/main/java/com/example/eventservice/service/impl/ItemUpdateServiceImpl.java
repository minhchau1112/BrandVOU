package com.example.eventservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.eventservice.exception.ItemNotFoundException;
import com.example.eventservice.model.item.dto.request.ItemUpdateRequest;
import com.example.eventservice.model.item.entity.ItemEntity;
import com.example.eventservice.model.item.mapper.ItemUpdateRequestToItemEntityMapper;
import com.example.eventservice.repository.ItemRepository;
import com.example.eventservice.service.ItemUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemUpdateServiceImpl implements ItemUpdateService {
    private final ItemRepository itemRepository;
    private final Cloudinary cloudinary;
    private final ItemUpdateRequestToItemEntityMapper itemUpdateRequestToItemEntityMapper = ItemUpdateRequestToItemEntityMapper.initialize();

    @Override
    public ItemEntity updateItemById(Long itemId, ItemUpdateRequest itemUpdateRequest) {
        final ItemEntity itemEntityToBeUpdate = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("With give itemID = " + itemId));

        String imageUrl = null;
        MultipartFile image = itemUpdateRequest.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                imageUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        itemUpdateRequestToItemEntityMapper.mapForUpdating(itemEntityToBeUpdate, imageUrl);

        ItemEntity updatedItemEntity = itemRepository.save(itemEntityToBeUpdate);

        return updatedItemEntity;
    }
}
