package com.example.eventservice.model.event.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateRequest {
    private Long brandId;
    private String name;
    private String image;
    private Integer voucherCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String gameType;
}
