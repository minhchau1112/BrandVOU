package com.example.brand_backend.controller;

import com.example.brand_backend.exception.ResourceNotFoundException;
import com.example.brand_backend.model.Events;
import com.example.brand_backend.model.Vouchers;
import com.example.brand_backend.repository.EventRepository;
import com.example.brand_backend.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/vouchers")
public class VoucherController {
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private EventRepository eventRepository;
    @PostMapping("/{eventId}")
    public ResponseEntity<Vouchers> createVoucher(
            @PathVariable Long eventId,
            @RequestParam("code") String code,
            @RequestParam("qrCode") String qrCode,
            @RequestParam("image") MultipartFile image,
            @RequestParam("value") Float value,
            @RequestParam("description") String description,
            @RequestParam("expirationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expirationDate,
            @RequestParam("status") String status) {

        Events event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        Vouchers voucher = new Vouchers();
        voucher.setCode(code);
        voucher.setQRCode(qrCode);
        voucher.setImage("image-url"); // Xử lý lưu trữ hình ảnh tùy thuộc vào nhu cầu
        voucher.setValue(value);
        voucher.setDescription(description);
        voucher.setExpirationDate(expirationDate);
        voucher.setStatus(status);
        voucher.setEvent(event);

        Vouchers savedVoucher = voucherRepository.save(voucher);
        return ResponseEntity.ok(savedVoucher);
    }
}
