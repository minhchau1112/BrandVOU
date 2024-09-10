package com.example.eventservice.service;

import com.example.eventservice.model.quiz.dto.request.RowQuestionResponse;
import com.example.eventservice.model.quiz.entity.QuizAnswerEntity;
import com.example.eventservice.model.quiz.entity.QuizQuestionsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizReadService {
    Page<QuizQuestionsEntity> getQuizQuestionsOfEvent(final Long eventgameId, Pageable pageable);
    Page<QuizAnswerEntity> getQuizAnswerOfEvent(final Long eventgameId, Pageable pageable);
    List<RowQuestionResponse> findQuizByEventGameId(final Long eventId, final Long gameId);

}
