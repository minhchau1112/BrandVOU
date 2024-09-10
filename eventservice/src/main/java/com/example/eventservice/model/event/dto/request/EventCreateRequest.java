package com.example.eventservice.model.event.dto.request;

import com.example.eventservice.model.quiz.dto.request.RowQuestionRequest;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateRequest {
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
