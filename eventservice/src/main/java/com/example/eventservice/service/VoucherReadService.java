package com.example.eventservice.service;

import com.example.eventservice.model.voucher.dto.response.StatisticsByVoucherTypeResponse;
import com.example.eventservice.model.voucher.dto.response.StatisticsTotalResponse;
import com.example.eventservice.model.voucher.entity.VoucherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoucherReadService {
    VoucherEntity getVoucherById(final Long voucherId);
    Page<VoucherEntity> getVouchersByBrandId(final Long brandId, String search, Pageable pageable);
    Page<VoucherEntity> findVouchersByEventId(final Long eventID, String searchTerm, Pageable pageable);
    List<StatisticsByVoucherTypeResponse> getVoucherStatsDetailByEvent(Long eventId);
    StatisticsTotalResponse getVoucherStatsGeneralByEvent(Long eventId);

}
