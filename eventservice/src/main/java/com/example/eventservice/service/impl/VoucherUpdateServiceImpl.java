package com.example.eventservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.eventservice.exception.EventAlreadyExistException;
import com.example.eventservice.exception.VoucherNotFoundException;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.voucher.dto.request.VoucherUpdateRequest;
import com.example.eventservice.model.voucher.entity.VoucherEntity;
import com.example.eventservice.model.voucher.mapper.VoucherUpdateRequestToVoucherEntityMapper;
import com.example.eventservice.repository.VoucherRepository;
import com.example.eventservice.service.EventReadService;
import com.example.eventservice.service.VoucherUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherUpdateServiceImpl implements VoucherUpdateService {
    private final VoucherRepository voucherRepository;
    private final EventReadService eventReadService;
    private final VoucherUpdateRequestToVoucherEntityMapper voucherUpdateRequestToVoucherEntityMapper = VoucherUpdateRequestToVoucherEntityMapper.initialize();
    private final Cloudinary cloudinary;
    @Override
    public VoucherEntity updateVoucherById(Long voucherId, VoucherUpdateRequest voucherUpdateRequest) {
        checkUniquenessCode(voucherUpdateRequest.getCode(), voucherUpdateRequest.getEventId(), voucherId);

        final VoucherEntity voucherEntityToBeUpdate = voucherRepository
                .findById(voucherId)
                .orElseThrow(() -> new VoucherNotFoundException("With give voucherID = " + voucherId));

        final EventEntity eventEntity = eventReadService.getEventById(voucherEntityToBeUpdate.getEvent().getId());

        String qrCodeUrl = null;
        MultipartFile qrCode = voucherUpdateRequest.getQRCode();
        if (qrCode != null && !qrCode.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(qrCode.getBytes(), ObjectUtils.emptyMap());
                qrCodeUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        String imageUrl = null;
        MultipartFile image = voucherUpdateRequest.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                imageUrl = (String) uploadResult.get("secure_url");
            } catch (IOException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        voucherUpdateRequestToVoucherEntityMapper.mapForUpdating(voucherEntityToBeUpdate, eventEntity, voucherUpdateRequest, qrCodeUrl, imageUrl);

        VoucherEntity updatedVoucherEntity = voucherRepository.save(voucherEntityToBeUpdate);

        return updatedVoucherEntity;
    }

    private void checkUniquenessCode(final String code, final Long eventId, final Long voucherId) {
        VoucherEntity exitsVoucher = voucherRepository.findByCodeAndEventId(code, eventId);

        if (exitsVoucher != null && !Objects.equals(exitsVoucher.getId(), voucherId)) {
            throw new EventAlreadyExistException("There is another voucher with given code");
        }
    }
}
