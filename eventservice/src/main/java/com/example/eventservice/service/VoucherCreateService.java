package com.example.eventservice.service;

import com.example.eventservice.model.voucher.dto.request.VoucherCreateRequest;
import com.example.eventservice.model.voucher.entity.VoucherEntity;

public interface VoucherCreateService {
    VoucherEntity createVoucherForEvent(final VoucherCreateRequest voucherCreateRequest);
}
