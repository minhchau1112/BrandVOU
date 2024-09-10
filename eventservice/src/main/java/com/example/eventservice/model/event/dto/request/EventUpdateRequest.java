package com.example.eventservice.model.event.dto.request;

import com.example.eventservice.model.quiz.dto.request.RowQuestionRequest;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventUpdateRequest {
    private Long brandId;
    private String name;
    private MultipartFile image;
    private Integer voucherCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String games;
    private String targetWord;
    private String questions;
}
