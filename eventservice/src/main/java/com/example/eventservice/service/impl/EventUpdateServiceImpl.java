package com.example.eventservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.eventservice.client.UserServiceClient;
import com.example.eventservice.exception.EventAlreadyExistException;
import com.example.eventservice.exception.EventNotFoundException;
import com.example.eventservice.model.event.dto.request.EventUpdateRequest;
import com.example.eventservice.model.event.entity.BrandEntity;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.event.mapper.EventUpdateRequestToEventEntityMapper;
import com.example.eventservice.repository.EventRepository;
import com.example.eventservice.service.EventUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventUpdateServiceImpl implements EventUpdateService {

    private final EventRepository eventRepository;
    private final EventUpdateRequestToEventEntityMapper eventUpdateRequestToEventEntityMapper = EventUpdateRequestToEventEntityMapper.initialize();
    private final UserServiceClient userServiceClient;
    private final Cloudinary cloudinary;

    @Override
    public EventEntity updateEventById(Long eventId, EventUpdateRequest eventUpdateRequest) {

        checkUniquenessEventName(eventUpdateRequest.getName(), eventUpdateRequest.getBrandId(), eventId);

        final EventEntity eventEntityToBeUpdate = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("With give eventID = " + eventId));

        // Extract the token from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getDetails();

        final BrandEntity brandEntity = userServiceClient.findBrandById(eventUpdateRequest.getBrandId(), "Bearer " + token);

        String imageUrl = null;
        MultipartFile image = eventUpdateRequest.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                imageUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        eventUpdateRequestToEventEntityMapper.mapForUpdating(eventEntityToBeUpdate, brandEntity, eventUpdateRequest, imageUrl);

        EventEntity updatedEventEntity = eventRepository.save(eventEntityToBeUpdate);

        return updatedEventEntity;
    }

    private void checkUniquenessEventName(final String eventName, final Long brandId, final Long eventId) {
        EventEntity exitsEvent = eventRepository.findByNameAndBrandId(eventName, brandId);
        if (exitsEvent != null && !Objects.equals(exitsEvent.getId(), eventId)) {
            throw new EventAlreadyExistException("There is another event with given name: " + eventName);
        }
    }
}
