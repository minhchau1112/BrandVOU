package com.example.eventservice.controller;

import com.example.eventservice.model.event.dto.request.EventCreateRequest;
import com.example.eventservice.model.event.dto.request.EventUpdateRequest;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.event.entity.EventGamesEntity;
import com.example.eventservice.model.item.dto.request.ItemCreateRequest;
import com.example.eventservice.model.quiz.dto.request.QuizAnswerCreateRequest;
import com.example.eventservice.model.quiz.dto.request.QuizQuestionCreateRequest;
import com.example.eventservice.model.quiz.dto.request.RowQuestionRequest;
import com.example.eventservice.model.quiz.entity.QuizAnswerEntity;
import com.example.eventservice.model.quiz.entity.QuizQuestionsEntity;
import com.example.eventservice.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventCreateService eventCreateService;
    private final EventReadService eventReadService;
    private final EventUpdateService eventUpdateService;
    private final ItemCreateService itemCreateService;
    private final NotificationReadService notificationReadService;
    private final EventGamesReadService eventGamesReadService;
    private final QuizCreateService quizCreateService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Long> createEventForBrand(@ModelAttribute final EventCreateRequest eventCreateRequest) {
        log.info("EventController | createEventForBrand");

        final EventEntity createdEvent = eventCreateService.createEventForBrand(eventCreateRequest);

        if (createdEvent != null) {
            String targetWord = createdEvent.getTargetWord();

            if (targetWord != null && !Objects.equals(targetWord, "")) {
                for (int index = 0; index < targetWord.length(); index++) {
                    String character = String.valueOf(targetWord.charAt(index));
                    ItemCreateRequest itemCreateRequest = new ItemCreateRequest(character, createdEvent.getId());
                    itemCreateService.createItemForShakeGame(itemCreateRequest);
                }
            }

            List<EventGamesEntity> eventGamesEntities = eventGamesReadService.findEventGamesEntityByEventIdAndType(createdEvent.getId(), "Quiz");

            ObjectMapper objectMapper = new ObjectMapper();
            List<RowQuestionRequest> rowQuestionRequests = null;
            try {
                rowQuestionRequests = objectMapper.readValue(eventCreateRequest.getQuestions(), new TypeReference<List<RowQuestionRequest>>(){});
            } catch (IOException e) {
                return ResponseEntity.ok((long) -1);
            }

            if (!rowQuestionRequests.isEmpty()) {
                for (int indexEventGames = 0; indexEventGames < eventGamesEntities.size(); indexEventGames++) {

                    EventGamesEntity eventGamesEntity = eventGamesEntities.get(indexEventGames);

                    for (int index = 0; index < rowQuestionRequests.size(); index++) {
                        RowQuestionRequest rowQuestionRequest = rowQuestionRequests.get(index);
                        QuizQuestionCreateRequest quizQuestionCreateRequest
                                = QuizQuestionCreateRequest
                                    .builder()
                                    .title(rowQuestionRequest.getQuestion())
                                    .eventgameId(eventGamesEntity.getId())
                                    .build();
                        QuizQuestionsEntity quizQuestionsEntity = quizCreateService.createQuizQuestionsForEventGame(quizQuestionCreateRequest);

                        if (quizQuestionsEntity != null) {
                            List<String> wrongAnswers = Arrays.asList(
                                    rowQuestionRequest.getWrongAnswer1(),
                                    rowQuestionRequest.getWrongAnswer2(),
                                    rowQuestionRequest.getWrongAnswer3()
                            );

                            try {
                                createQuizAnswer(rowQuestionRequest.getCorrectAnswer(), true, quizQuestionsEntity.getId());

                                for (String answer : wrongAnswers) {
                                    createQuizAnswer(answer, false, quizQuestionsEntity.getId());
                                }
                            } catch (Exception e) {
                                System.err.println("Error while creating quiz answers: " + e.getMessage());
                            }
                        }

                    }
                }
            }

            return ResponseEntity.ok(createdEvent.getId());
        }
        return ResponseEntity.ok((long) -1);
    }

    public QuizAnswerEntity createQuizAnswer(String answerText, boolean isCorrect, Long questionId) {
        QuizAnswerCreateRequest request = QuizAnswerCreateRequest.builder()
                .answerText(answerText)
                .isCorrect(isCorrect)
                .questionId(questionId)
                .build();
        return quizCreateService.createQuizAnswerForEventGame(request);
    }

    @GetMapping("/brand/{brandId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<Page<EventEntity>> getEventsByBrand(
            @PathVariable Long brandId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String searchTerm) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<EventEntity> eventsPage = eventReadService.getEventsByBrandId(brandId, searchTerm, pageable);

            return ResponseEntity.ok(eventsPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<EventEntity> getEventByEventId(@PathVariable Long eventId) {
        EventEntity event = eventReadService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<EventEntity> updateEvent(@PathVariable Long eventId, @ModelAttribute final EventUpdateRequest eventUpdateRequest) {
        EventEntity eventEntity = eventUpdateService.updateEventById(eventId, eventUpdateRequest);

        return ResponseEntity.ok(eventEntity);
    }

    @GetMapping("/brand/all/{brandId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<List<EventEntity>> getAllEventsByBrandId(@PathVariable Long brandId) {
        List<EventEntity> eventEntityList = eventReadService.getAllEventsByBrandId(brandId);

        return ResponseEntity.ok(eventEntityList);
    }

    @GetMapping("/brand/have-target-word/{brandId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<List<EventEntity>> getAllEventsByBrandIdHaveTargetWord(@PathVariable Long brandId) {
        List<EventEntity> eventEntityList = eventReadService.findEventsOfBrandHaveTargetWord(brandId);

        return ResponseEntity.ok(eventEntityList);
    }

    @GetMapping("/hot-events")
    public ResponseEntity<Page<Object[]>> getEventsWithNotificationStatus(
            @RequestParam Long playerId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String searchTerm) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<Object[]> eventsPage = eventReadService.findEventsWithNotificationStatus(playerId, searchTerm, pageable);

            return ResponseEntity.ok(eventsPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/favourite-events")
    public ResponseEntity<Page<EventEntity>> getFavouriteEvents(
            @RequestParam Long playerId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String searchTerm) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<EventEntity> eventsPage = notificationReadService.findFavouriteEvent(playerId, searchTerm, pageable);

            return ResponseEntity.ok(eventsPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
