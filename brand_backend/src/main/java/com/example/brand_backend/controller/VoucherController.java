package com.example.brand_backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.brand_backend.exception.ResourceNotFoundException;
import com.example.brand_backend.model.Events;
import com.example.brand_backend.model.Vouchers;
import com.example.brand_backend.repository.EventRepository;
import com.example.brand_backend.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/vouchers")
public class VoucherController {
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/{eventId}")
    public ResponseEntity<Vouchers> createVoucher(
            @PathVariable Long eventId,
            @RequestParam("code") String code,
            @RequestParam("qrCode") MultipartFile qrCode,
            @RequestParam("image") MultipartFile image,
            @RequestParam("value") Float value,
            @RequestParam("description") String description,
            @RequestParam("expirationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expirationDate,
            @RequestParam("status") String status) {

        Events event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        String qrCodeUrl = null;
        if (qrCode != null && !qrCode.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(qrCode.getBytes(), ObjectUtils.emptyMap());
                qrCodeUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                imageUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        Vouchers voucher = new Vouchers();
        voucher.setCode(code);
        voucher.setQRCode(qrCodeUrl);
        voucher.setImage(imageUrl);
        voucher.setValue(value);
        voucher.setDescription(description);
        voucher.setExpirationDate(expirationDate);
        voucher.setStatus(status);
        voucher.setEvent(event);

        Vouchers savedVoucher = voucherRepository.save(voucher);
        return ResponseEntity.ok(savedVoucher);
    }
    @GetMapping("/{brandId}")
    public ResponseEntity<List<Vouchers>> getVouchersByBrand(@PathVariable Long brandId) {
        try {
            System.out.println("Brand ID: " + brandId);
            List<Vouchers> vouchers = voucherRepository.findVouchersByBrandId(brandId);
            System.out.println("Found vouchers: " + vouchers.size());
            for(int i = 0; i < vouchers.size(); i++) {
                System.out.println(vouchers.get(i).getCode());
            }
            if (vouchers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(vouchers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/view-detail/{voucherId}")
    public ResponseEntity<Vouchers> getVoucherByVoucherId(@PathVariable Long voucherId) {
        Optional<Vouchers> voucher = voucherRepository.findById(voucherId);
        if (voucher.isPresent()) {
            Vouchers result = voucher.get();
            return ResponseEntity.ok(result);
        } else {
            throw new ResourceNotFoundException("Voucher not found with id " + voucherId);
        }
    }
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Vouchers>> getVoucherByEventId(@PathVariable Long eventId) {
        try {
            List<Vouchers> vouchers = voucherRepository.findVouchersByEventId(eventId);
            System.out.println("Found vouchers: " + vouchers.size());
            for(int i = 0; i < vouchers.size(); i++) {
                System.out.println(vouchers.get(i).getCode());
            }
            if (vouchers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(vouchers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/update/{voucherId}")
    public ResponseEntity<Vouchers> updateVoucher(
            @PathVariable Long voucherId,
            @RequestParam("eventId") Long eventId,
            @RequestParam("code") String code,
            @RequestParam(value = "qrCode") MultipartFile qrCode,
            @RequestParam(value = "image") MultipartFile image,
            @RequestParam("value") Float value,
            @RequestParam("description") String description,
            @RequestParam("expirationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expirationDate,
            @RequestParam("status") String status
            ) {

        Events event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        Vouchers voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        voucher.setCode(code);
        voucher.setValue(value);
        voucher.setDescription(description);
        voucher.setExpirationDate(expirationDate);
        voucher.setStatus(status);
        voucher.setEvent(event);

        if (qrCode != null && !qrCode.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(qrCode.getBytes(), ObjectUtils.emptyMap());
                String qrCodeUrl = (String) uploadResult.get("secure_url");
                voucher.setQRCode(qrCodeUrl);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("secure_url");
                voucher.setImage(imageUrl);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        Vouchers updatedVoucher = voucherRepository.save(voucher);
        return ResponseEntity.ok(updatedVoucher);
    }

    @DeleteMapping("/delete/{voucherId}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long voucherId) {
        if (!voucherRepository.existsById(voucherId)) {
            return ResponseEntity.notFound().build();
        }
        voucherRepository.deleteById(voucherId);
        return ResponseEntity.noContent().build();
    }
}
