package com.example.eventservice.model.quiz.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswerCreateRequest {
    private Long questionId;
    private String answerText;
    private Boolean isCorrect;
}
