package com.example.eventservice.model.quiz.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.quiz.dto.request.QuizAnswerCreateRequest;
import com.example.eventservice.model.quiz.dto.request.QuizQuestionCreateRequest;
import com.example.eventservice.model.quiz.entity.QuizAnswerEntity;
import com.example.eventservice.model.quiz.entity.QuizQuestionsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuizAnswerCreateRequestToQuizAnswerEntityMapper extends BaseMapper<QuizAnswerCreateRequest, QuizAnswerEntity> {

    @Named("mapForSaving")
    default QuizAnswerEntity mapForSaving(QuizAnswerCreateRequest quizAnswerCreateRequest) {

        return QuizAnswerEntity.builder()
                .answerText(quizAnswerCreateRequest.getAnswerText())
                .isCorrect(quizAnswerCreateRequest.getIsCorrect())
                .build();
    }

    static QuizAnswerCreateRequestToQuizAnswerEntityMapper initialize() {
        return Mappers.getMapper(QuizAnswerCreateRequestToQuizAnswerEntityMapper.class);
    }
}
