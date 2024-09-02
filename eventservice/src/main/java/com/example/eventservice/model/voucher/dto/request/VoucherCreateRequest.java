package com.example.eventservice.model.voucher.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherCreateRequest {
    private Long eventId;
    private String code;
    private MultipartFile QRCode;
    private MultipartFile image;
    private Float value;
    private String description;
    private LocalDateTime expirationDate;
    private String status;
}
