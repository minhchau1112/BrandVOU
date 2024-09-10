package com.example.eventservice.controller;

import com.example.eventservice.model.quiz.dto.request.QuizQuestionCreateRequest;
import com.example.eventservice.model.quiz.dto.request.RowQuestionResponse;
import com.example.eventservice.model.quiz.entity.QuizQuestionsEntity;
import com.example.eventservice.service.QuizCreateService;
import com.example.eventservice.service.QuizDeleteService;
import com.example.eventservice.service.QuizReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/events/quiz-question")
@RequiredArgsConstructor
@Validated
public class QuizQuestionController {
    private final QuizCreateService quizCreateService;
    private final QuizReadService quizReadService;
    private final QuizDeleteService quizDeleteService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Long> createQuizQuestionForEvent(@RequestBody final QuizQuestionCreateRequest quizQuestionCreateRequest) {
        log.info("QuizQuestionController | createQuizQuestionForEvent");

        final QuizQuestionsEntity createdQuizQuestion = quizCreateService.createQuizQuestionsForEventGame(quizQuestionCreateRequest);

        if (createdQuizQuestion != null) {

            return ResponseEntity.ok(createdQuizQuestion.getId());
        }

        return ResponseEntity.ok((long) -1);
    }

    @GetMapping("/event-games/{eventgameId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<Page<QuizQuestionsEntity>> getQuizQuestionsByEventGame(
            @PathVariable Long eventgameId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        log.info("QuizQuestionController | getQuizQuestionsByEventGame");

        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<QuizQuestionsEntity> quizQuestionsEntityPage = quizReadService.getQuizQuestionsOfEvent(eventgameId, pageable);

            return ResponseEntity.ok(quizQuestionsEntityPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{quizquestionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Void> deleteQuizQuestion(@PathVariable Long quizquestionId) {
        log.info("QuizQuestionController | deleteQuizQuestion");

        return quizDeleteService.deleteQuizQuestion(quizquestionId);
    }

    @DeleteMapping("/event-games/{eventgameId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Void> deleteAllQuizQuestionOfEvent(@PathVariable Long eventgameId) {
        log.info("QuizQuestionController | deleteAllQuizQuestionOfEvent");

        return quizDeleteService.deleteAllQuizQuestionOfEvent(eventgameId);
    }

    @GetMapping("/event-games/existed")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<List<RowQuestionResponse>> getQuizByEventGame(@RequestParam Long eventId, @RequestParam Long gameId) {
        log.info("QuizQuestionController | getQuizByEventGame");

        List<RowQuestionResponse> rowQuestionResponses = quizReadService.findQuizByEventGameId(eventId, gameId);

        return ResponseEntity.ok(rowQuestionResponses);
    }
}
