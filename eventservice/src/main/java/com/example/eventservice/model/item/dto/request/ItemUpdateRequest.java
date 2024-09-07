package com.example.eventservice.model.item.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemUpdateRequest {
    private MultipartFile image;
}
