package com.example.eventservice.model.quiz.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestionCreateRequest {
    private Long eventgameId;
    private String title;
    private String description;
}
