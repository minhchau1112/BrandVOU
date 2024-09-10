package com.example.eventservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.eventservice.client.UserServiceClient;
import com.example.eventservice.exception.EventAlreadyExistException;
import com.example.eventservice.exception.EventNotFoundException;
import com.example.eventservice.model.event.dto.request.EventUpdateRequest;
import com.example.eventservice.model.event.entity.BrandEntity;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.event.entity.EventGamesEntity;
import com.example.eventservice.model.event.mapper.EventUpdateRequestToEventEntityMapper;
import com.example.eventservice.model.item.dto.request.ItemCreateRequest;
import com.example.eventservice.model.quiz.dto.request.QuizAnswerCreateRequest;
import com.example.eventservice.model.quiz.dto.request.QuizQuestionCreateRequest;
import com.example.eventservice.model.quiz.dto.request.RowQuestionRequest;
import com.example.eventservice.model.quiz.entity.QuizAnswerEntity;
import com.example.eventservice.model.quiz.entity.QuizQuestionsEntity;
import com.example.eventservice.repository.EventRepository;
import com.example.eventservice.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
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
    private final EventGamesReadService eventGamesReadService;
    private final QuizDeleteService quizDeleteService;
    private final QuizCreateService quizCreateService;
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

        log.info("EventUpdateServiceImpl | eventUpdateRequest.getGames() | " + eventUpdateRequest.getGames());

        String[] gamesId = eventUpdateRequest.getGames().split(";");

        if (gamesId.length > 0) {
            eventUpdateRequestToEventEntityMapper.mapForUpdating(eventEntityToBeUpdate, eventUpdateRequest, imageUrl);

            EventEntity updatedEventEntity = eventRepository.save(eventEntityToBeUpdate);

            eventGamesDeleteService.deleteEventsGamesByEventId(updatedEventEntity.getId());

            log.info("EventUpdateServiceImpl | deleteEventsGamesByEventId");

            for (String gameId : gamesId) {
                log.info("EventUpdateServiceImpl | createEventGames | gameId=" + gameId);

                eventGamesCreateService.createEventGames(updatedEventEntity.getId(), Long.parseLong(gameId));
            }

            log.info("EventUpdateServiceImpl | createEventGames");

            String targetWord = updatedEventEntity.getTargetWord();
            if (targetWord != null && !Objects.equals(targetWord, "")) {
                for (int index = 0; index < targetWord.length(); index++) {
                    String character = String.valueOf(targetWord.charAt(index));
                    ItemCreateRequest itemCreateRequest = new ItemCreateRequest(character, updatedEventEntity.getId());
                    itemCreateService.createItemForShakeGame(itemCreateRequest);
                }
            }

            List<EventGamesEntity> eventGamesEntities = eventGamesReadService.findEventGamesEntityByEventIdAndType(updatedEventEntity.getId(), "Quiz");

            log.info("EventUpdateServiceImpl | findEventGamesEntityByEventIdAndType");

            log.info("EventUpdateServiceImpl | findEventGamesEntityByEventIdAndType | length = " + eventGamesEntities.size());

            for (int indexEventGames = 0; indexEventGames < eventGamesEntities.size(); indexEventGames++) {
                log.info("EventUpdateServiceImpl | eventGamesEntities.get | " + eventGamesEntities.get(indexEventGames).getId());

                EventGamesEntity eventGamesEntity = eventGamesEntities.get(indexEventGames);

                quizDeleteService.deleteAllQuizAnswerOfEvent(eventGamesEntity.getId());

                log.info("EventUpdateServiceImpl | deleteAllQuizAnswerOfEvent | " + eventGamesEntities.get(indexEventGames).getId());

                quizDeleteService.deleteAllQuizQuestionOfEvent(eventGamesEntity.getId());

                log.info("EventUpdateServiceImpl | deleteAllQuizQuestionOfEvent | " + eventGamesEntities.get(indexEventGames).getId());

            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<RowQuestionRequest> rowQuestionRequests = null;
            try {
                rowQuestionRequests = objectMapper.readValue(eventUpdateRequest.getQuestions(), new TypeReference<List<RowQuestionRequest>>(){});
            } catch (IOException e) {
                return null;
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

            return updatedEventEntity;
        }

        return null;
    }

    public QuizAnswerEntity createQuizAnswer(String answerText, boolean isCorrect, Long questionId) {
        QuizAnswerCreateRequest request = QuizAnswerCreateRequest.builder()
                .answerText(answerText)
                .isCorrect(isCorrect)
                .questionId(questionId)
                .build();
        return quizCreateService.createQuizAnswerForEventGame(request);
    }

    private void checkUniquenessEventName(final String eventName, final Long brandId, final Long eventId) {
        EventEntity exitsEvent = eventRepository.findByNameAndBrandId(eventName, brandId);
        if (exitsEvent != null && !Objects.equals(exitsEvent.getId(), eventId)) {
            throw new EventAlreadyExistException("There is another event with given name: " + eventName);
        }
    }
}
