package com.example.eventservice.service.impl;

import com.example.eventservice.exception.VoucherNotFoundException;
import com.example.eventservice.model.voucher.entity.VoucherEntity;
import com.example.eventservice.repository.VoucherRepository;
import com.example.eventservice.service.VoucherReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherReadServiceImpl implements VoucherReadService {

    private final VoucherRepository voucherRepository;

    @Override
    public VoucherEntity getVoucherById(Long voucherId) {
        Optional<VoucherEntity> voucherEntity = voucherRepository.findById(voucherId);
        if (voucherEntity.isEmpty()) {
            throw new VoucherNotFoundException("Couldn't find any Voucher");
        }
        return voucherEntity.get();
    }

    @Override
    public Page<VoucherEntity> getVouchersByBrandId(Long brandId, String searchTerm, Pageable pageable) {
        return voucherRepository.findVouchersByBrandId(brandId, searchTerm, pageable);
    }

    @Override
    public Page<VoucherEntity> findVouchersByEventId(Long eventID, String searchTerm, Pageable pageable) {
        return voucherRepository.findVouchersByEventId(eventID, searchTerm, pageable);
    }
}

