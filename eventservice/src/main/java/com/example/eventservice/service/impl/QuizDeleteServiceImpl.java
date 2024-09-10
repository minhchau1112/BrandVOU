package com.example.eventservice.service.impl;

import com.example.eventservice.repository.QuizAnswerRepository;
import com.example.eventservice.repository.QuizQuestionsRepository;
import com.example.eventservice.service.QuizDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizDeleteServiceImpl implements QuizDeleteService {
    private final QuizQuestionsRepository quizQuestionsRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    @Override
    public ResponseEntity<Void> deleteQuizQuestion(Long quizQuestionId) {
        quizQuestionsRepository.deleteById(quizQuestionId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteQuizAnswer(Long quizAnswerId) {
        quizAnswerRepository.deleteById(quizAnswerId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteAllQuizQuestionOfEvent(Long eventgameId) {
        quizQuestionsRepository.deleteAllQuizQuestionOfEventGame(eventgameId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteAllQuizAnswerOfEvent(Long eventgameId) {
        quizAnswerRepository.deleteAllQuizAnswerOfEventGame(eventgameId);
        return ResponseEntity.ok().build();
    }
}
