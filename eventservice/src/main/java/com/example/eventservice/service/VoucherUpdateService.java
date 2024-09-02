package com.example.eventservice.service;

import com.example.eventservice.model.voucher.dto.request.VoucherUpdateRequest;
import com.example.eventservice.model.voucher.entity.VoucherEntity;

public interface VoucherUpdateService {
    VoucherEntity updateVoucherById(final Long voucherId, final VoucherUpdateRequest voucherUpdateRequest);
}
