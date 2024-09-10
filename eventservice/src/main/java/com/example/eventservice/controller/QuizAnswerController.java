package com.example.eventservice.controller;

import com.example.eventservice.model.quiz.dto.request.QuizAnswerCreateRequest;
import com.example.eventservice.model.quiz.dto.request.QuizQuestionCreateRequest;
import com.example.eventservice.model.quiz.entity.QuizAnswerEntity;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/events/quiz-answer")
@RequiredArgsConstructor
@Validated
public class QuizAnswerController {
    private final QuizCreateService quizCreateService;
    private final QuizReadService quizReadService;
    private final QuizDeleteService quizDeleteService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Long> createQuizAnswerForEvent(@RequestBody final QuizAnswerCreateRequest quizAnswerCreateRequest) {
        log.info("QuizAnswerController | createQuizQuestionForEvent");

        final QuizAnswerEntity createdQuizAnswer = quizCreateService.createQuizAnswerForEventGame(quizAnswerCreateRequest);

        if (createdQuizAnswer != null) {

            return ResponseEntity.ok(createdQuizAnswer.getId());
        }

        return ResponseEntity.ok((long) -1);
    }

    @GetMapping("/event-games/{eventgameId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<Page<QuizAnswerEntity>> getQuizAnswerByEventGame(
            @PathVariable Long eventgameId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        log.info("QuizAnswerController | getQuizAnswerByEventGame");

        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<QuizAnswerEntity> quizAnswerEntityPage = quizReadService.getQuizAnswerOfEvent(eventgameId, pageable);

            return ResponseEntity.ok(quizAnswerEntityPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{quizanswerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Void> deleteQuizAnswer(@PathVariable Long quizanswerId) {
        log.info("QuizAnswerController | deleteQuizAnswer");

        return quizDeleteService.deleteQuizAnswer(quizanswerId);
    }

    @DeleteMapping("/event-games/{eventgameId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Void> deleteAllQuizAnswerOfEvent(@PathVariable Long eventgameId) {
        log.info("QuizAnswerController | deleteAllQuizAnswerOfEvent");

        return quizDeleteService.deleteAllQuizAnswerOfEvent(eventgameId);
    }
}
