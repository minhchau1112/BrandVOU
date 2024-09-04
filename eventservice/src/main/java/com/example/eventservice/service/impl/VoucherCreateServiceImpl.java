package com.example.eventservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.eventservice.exception.VoucherAlreadyExistException;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.voucher.dto.request.VoucherCreateRequest;
import com.example.eventservice.model.voucher.entity.VoucherEntity;
import com.example.eventservice.model.voucher.mapper.VoucherCreateRequestToVoucherEntityMapper;
import com.example.eventservice.repository.VoucherRepository;
import com.example.eventservice.service.EventReadService;
import com.example.eventservice.service.VoucherCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherCreateServiceImpl implements VoucherCreateService {
    private final VoucherRepository voucherRepository;
    private final EventReadService eventReadService;
    private final VoucherCreateRequestToVoucherEntityMapper voucherCreateRequestToVoucherEntityMapper = VoucherCreateRequestToVoucherEntityMapper.initialize();
    private final Cloudinary cloudinary;
    @Override
    public VoucherEntity createVoucherForEvent(VoucherCreateRequest voucherCreateRequest) {
        if (!checkUniquenessCode(voucherCreateRequest.getCode(), voucherCreateRequest.getEventId())) {
            return null;
        }

        final EventEntity eventEntity = eventReadService.getEventById(voucherCreateRequest.getEventId());

        String qrCodeUrl = null;
        MultipartFile qrCode = voucherCreateRequest.getQRCode();
        if (qrCode != null && !qrCode.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(qrCode.getBytes(), ObjectUtils.emptyMap());
                qrCodeUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        String imageUrl = null;
        MultipartFile image = voucherCreateRequest.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                imageUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        final VoucherEntity voucherEntity = voucherCreateRequestToVoucherEntityMapper.mapForSaving(voucherCreateRequest, eventEntity, qrCodeUrl, imageUrl);

        VoucherEntity savedVoucherEntity = voucherRepository.save(voucherEntity);

        return savedVoucherEntity;
    }

    private boolean checkUniquenessCode(final String code, final Long eventId) {
        if (voucherRepository.existsByCodeAndEventId(code,  eventId)) {
            return false;
//            throw new VoucherAlreadyExistException("There is another voucher with given code");
        }
        return true;
    }
}
