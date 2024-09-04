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
import com.example.eventservice.service.EventGamesCreateService;
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
    private final EventGamesCreateService eventGamesCreateService;

    @Override
    public EventEntity createEventForBrand(EventCreateRequest eventCreateRequest) {
        if (!checkUniquenessEventName(eventCreateRequest.getName(), eventCreateRequest.getBrandId())) {
            return null;
        }

        log.info("createEventForBrand: " + eventCreateRequest.getGames());
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

        String[] gamesId = eventCreateRequest.getGames().split(";");

        if (gamesId.length > 0) {
            final EventEntity eventEntity = eventCreateRequestToEventEntityMapper.mapForSaving(eventCreateRequest, brandEntity, imageUrl);
            EventEntity savedEventEntity = eventRepository.save(eventEntity);

            for (String gameId : gamesId) {
                eventGamesCreateService.createEventGames(savedEventEntity.getId(), Long.parseLong(gameId));
            }

            return savedEventEntity;
        }

        return null;
    }

    private boolean checkUniquenessEventName(final String eventName, final Long brandId) {
        if (eventRepository.existsByNameAndBrandId(eventName,  brandId)) {
            return false;
//            throw new EventAlreadyExistException("There is another event with given name: " + eventName);
        }
        return true;
    }
}
