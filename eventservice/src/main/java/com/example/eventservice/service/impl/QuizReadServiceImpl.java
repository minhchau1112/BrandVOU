package com.example.eventservice.service.impl;

import com.example.eventservice.model.quiz.dto.request.RowQuestionResponse;
import com.example.eventservice.model.quiz.entity.QuizAnswerEntity;
import com.example.eventservice.model.quiz.entity.QuizQuestionsEntity;
import com.example.eventservice.repository.QuizAnswerRepository;
import com.example.eventservice.repository.QuizQuestionsRepository;
import com.example.eventservice.service.QuizReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizReadServiceImpl implements QuizReadService {
    private final QuizQuestionsRepository quizQuestionsRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    @Override
    public Page<QuizQuestionsEntity> getQuizQuestionsOfEvent(Long eventgameId, Pageable pageable) {
        return quizQuestionsRepository.getQuizQuestionsOfEventGame(eventgameId, pageable);
    }

    @Override
    public Page<QuizAnswerEntity> getQuizAnswerOfEvent(Long eventgameId, Pageable pageable) {
        return quizAnswerRepository.getQuizAnswerOfEventGame(eventgameId, pageable);
    }

    @Override
    public List<RowQuestionResponse> findQuizByEventGameId(Long eventId, Long gameId) {
        return quizQuestionsRepository.findQuizByEventGameId(eventId, gameId);
    }
}
