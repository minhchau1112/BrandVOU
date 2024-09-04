package com.example.eventservice.model.event.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
}
