package com.example.eventservice.model.quiz.dto.request;

import lombok.Getter;

@Getter
public class RowQuestionRequest {
    private String question;
    private String correctAnswer;
    private String wrongAnswer1;
    private String wrongAnswer2;
    private String wrongAnswer3;
}
