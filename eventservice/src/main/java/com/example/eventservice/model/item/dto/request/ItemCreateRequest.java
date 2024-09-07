package com.example.eventservice.model.item.dto.request;

import com.example.eventservice.model.event.entity.EventEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCreateRequest {
    private String name;
    private MultipartFile image;
    private Long eventId;
    private String type;
}
