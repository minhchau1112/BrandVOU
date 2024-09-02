package com.example.eventservice.service.impl;

import com.example.eventservice.repository.VoucherRepository;
import com.example.eventservice.service.VoucherDeleteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherDeleteServiceImpl implements VoucherDeleteService {
    private final VoucherRepository voucherRepository;
    @Override
    public ResponseEntity<Void> deleteVoucher(Long voucherId) {
        if (!voucherRepository.existsById(voucherId)) {
            return ResponseEntity.notFound().build();
        }
        voucherRepository.deleteById(voucherId);
        return ResponseEntity.noContent().build();
    }
}
