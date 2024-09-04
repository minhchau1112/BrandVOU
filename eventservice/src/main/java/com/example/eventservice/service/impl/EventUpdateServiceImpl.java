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
import com.example.eventservice.model.item.dto.request.ItemCreateRequest;
import com.example.eventservice.repository.EventRepository;
import com.example.eventservice.service.*;
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
    private final Cloudinary cloudinary;
    private final EventGamesCreateService eventGamesCreateService;
    private final EventGamesDeleteService eventGamesDeleteService;
    private final ItemDeleteService itemDeleteService;
    private final ItemCreateService itemCreateService;

    @Override
    public EventEntity updateEventById(Long eventId, EventUpdateRequest eventUpdateRequest) {

        checkUniquenessEventName(eventUpdateRequest.getName(), eventUpdateRequest.getBrandId(), eventId);

        final EventEntity eventEntityToBeUpdate = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("With give eventID = " + eventId));

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

        String targetWordOld = eventEntityToBeUpdate.getTargetWord();
        if (!Objects.equals(targetWordOld, eventUpdateRequest.getTargetWord())) {
            itemDeleteService.deleteItemsOfEvent(eventEntityToBeUpdate.getId());
        }

        String[] gamesId = eventUpdateRequest.getGames().split(";");

        if (gamesId.length > 0) {
            eventUpdateRequestToEventEntityMapper.mapForUpdating(eventEntityToBeUpdate, eventUpdateRequest, imageUrl);

            EventEntity updatedEventEntity = eventRepository.save(eventEntityToBeUpdate);

            eventGamesDeleteService.deleteEventsGamesByEventId(updatedEventEntity.getId());

            for (String gameId : gamesId) {
                eventGamesCreateService.createEventGames(updatedEventEntity.getId(), Long.parseLong(gameId));
            }

            String targetWord = updatedEventEntity.getTargetWord();
            if (targetWord != null && !Objects.equals(targetWord, "")) {
                for (int index = 0; index < targetWord.length(); index++) {
                    String character = String.valueOf(targetWord.charAt(index));
                    ItemCreateRequest itemCreateRequest = new ItemCreateRequest(character, null, updatedEventEntity.getId(), "Puzzle Piece");
                    itemCreateService.createItemForShakeGame(itemCreateRequest);
                }
            }

            return updatedEventEntity;
        }

        return null;
    }

    private void checkUniquenessEventName(final String eventName, final Long brandId, final Long eventId) {
        EventEntity exitsEvent = eventRepository.findByNameAndBrandId(eventName, brandId);
        if (exitsEvent != null && !Objects.equals(exitsEvent.getId(), eventId)) {
            throw new EventAlreadyExistException("There is another event with given name: " + eventName);
        }
    }
}
