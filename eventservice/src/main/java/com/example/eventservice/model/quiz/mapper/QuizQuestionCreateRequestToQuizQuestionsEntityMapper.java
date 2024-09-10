package com.example.eventservice.model.quiz.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.quiz.dto.request.QuizQuestionCreateRequest;
import com.example.eventservice.model.quiz.entity.QuizQuestionsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuizQuestionCreateRequestToQuizQuestionsEntityMapper extends BaseMapper<QuizQuestionCreateRequest, QuizQuestionsEntity> {

    @Named("mapForSaving")
    default QuizQuestionsEntity mapForSaving(QuizQuestionCreateRequest quizQuestionCreateRequest) {

        return QuizQuestionsEntity.builder()
                .title(quizQuestionCreateRequest.getTitle())
                .description(quizQuestionCreateRequest.getDescription())
                .build();
    }

    static QuizQuestionCreateRequestToQuizQuestionsEntityMapper initialize() {
        return Mappers.getMapper(QuizQuestionCreateRequestToQuizQuestionsEntityMapper.class);
    }
}
