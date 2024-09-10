package com.example.eventservice.service;

import com.example.eventservice.model.quiz.dto.request.QuizAnswerCreateRequest;
import com.example.eventservice.model.quiz.dto.request.QuizQuestionCreateRequest;
import com.example.eventservice.model.quiz.entity.QuizAnswerEntity;
import com.example.eventservice.model.quiz.entity.QuizQuestionsEntity;

public interface QuizCreateService {
    QuizQuestionsEntity createQuizQuestionsForEventGame(final QuizQuestionCreateRequest quizQuestionCreateRequest);
    QuizAnswerEntity createQuizAnswerForEventGame(final QuizAnswerCreateRequest quizAnswerCreateRequest);
}
