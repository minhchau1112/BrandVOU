package com.example.eventservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.eventservice.client.UserServiceClient;
import com.example.eventservice.exception.EventAlreadyExistException;
import com.example.eventservice.model.event.dto.request.EventCreateRequest;
import com.example.eventservice.model.event.entity.BrandEntity;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.event.mapper.EventCreateRequestToEventEntityMapper;
import com.example.eventservice.repository.EventRepository;
import com.example.eventservice.service.EventCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventCreateServiceImpl implements EventCreateService {
    private final EventRepository eventRepository;
    private final EventCreateRequestToEventEntityMapper eventCreateRequestToEventEntityMapper = EventCreateRequestToEventEntityMapper.initialize();
    private final UserServiceClient userServiceClient;
    private final Cloudinary cloudinary;

    @Override
    public EventEntity createEventForBrand(EventCreateRequest eventCreateRequest) {
        checkUniquenessEventName(eventCreateRequest.getName(), eventCreateRequest.getBrandId());

        log.info("createEventForBrand: " + eventCreateRequest);
        // Extract the token from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();

        final BrandEntity brandEntity = userServiceClient.findBrandById(eventCreateRequest.getBrandId(), "Bearer " + token);

        String imageUrl = null;
        MultipartFile image = eventCreateRequest.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                imageUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        final EventEntity eventEntity = eventCreateRequestToEventEntityMapper.mapForSaving(eventCreateRequest, brandEntity, imageUrl);
        EventEntity savedEventEntity = eventRepository.save(eventEntity);

        return savedEventEntity;
    }

    private void checkUniquenessEventName(final String eventName, final Long brandId) {
        if (eventRepository.existsByNameAndBrandId(eventName,  brandId)) {
            throw new EventAlreadyExistException("There is another event with given name: " + eventName);
        }
    }
}
