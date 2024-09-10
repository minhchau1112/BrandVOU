package com.example.eventservice.model.item.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCreateRequest {
    private String name;
    private Long eventId;
}
