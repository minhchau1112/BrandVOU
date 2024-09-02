package com.example.eventservice.service;

import com.example.eventservice.model.voucher.entity.VoucherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoucherReadService {
    VoucherEntity getVoucherById(final Long voucherId);
    Page<VoucherEntity> getVouchersByBrandId(final Long brandId, String search, Pageable pageable);
    Page<VoucherEntity> findVouchersByEventId(final Long eventID, String searchTerm, Pageable pageable);
}
