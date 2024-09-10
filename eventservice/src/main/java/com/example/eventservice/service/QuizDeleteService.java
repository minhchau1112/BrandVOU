package com.example.eventservice.service;

import org.springframework.http.ResponseEntity;

public interface QuizDeleteService {
    ResponseEntity<Void> deleteQuizQuestion(Long quizQuestionId);
    ResponseEntity<Void> deleteQuizAnswer(Long quizAnswerId);
    ResponseEntity<Void> deleteAllQuizQuestionOfEvent(Long eventgameId);
    ResponseEntity<Void> deleteAllQuizAnswerOfEvent(Long eventgameId);
}
