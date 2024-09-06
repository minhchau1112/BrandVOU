package com.example.eventservice.service.impl;

import com.example.eventservice.exception.VoucherNotFoundException;
import com.example.eventservice.model.voucher.dto.response.StatisticsByVoucherTypeResponse;
import com.example.eventservice.model.voucher.dto.response.StatisticsTotalResponse;
import com.example.eventservice.model.voucher.entity.VoucherEntity;
import com.example.eventservice.repository.VoucherRepository;
import com.example.eventservice.service.VoucherReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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

    @Override
    public List<StatisticsByVoucherTypeResponse> getVoucherStatsDetailByEvent(Long eventId) {
        List<Object[]> list = voucherRepository.getVoucherStatsDetailByEvent(eventId);
        List<StatisticsByVoucherTypeResponse> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            StatisticsByVoucherTypeResponse response = new StatisticsByVoucherTypeResponse((String) list.get(i)[0], (Integer) list.get(i)[1], (Long) list.get(i)[2], (Long) list.get(i)[3], (Long) list.get(i)[4]);
            result.add(response);
        }

        return result;
    }

    @Override
    public StatisticsTotalResponse getVoucherStatsGeneralByEvent(Long eventId) {
        Object[] resultArray = voucherRepository.getVoucherStatsGeneralByEvent(eventId);

        Object[] innerArray = (Object[]) resultArray[0];

        Long totalVoucherTypes = ((Number) innerArray[0]).longValue();
        Long totalVoucherCount = ((Number) innerArray[1]).longValue();
        Long totalExchangedVouchers = ((Number) innerArray[2]).longValue();
        Long totalUsedVouchers = ((Number) innerArray[3]).longValue();
        Long expiredVouchers = ((Number) innerArray[4]).longValue();
        Long unexpiredVouchers = ((Number) innerArray[5]).longValue();
        Long remainingVouchers = ((Number) innerArray[6]).longValue();

        return new StatisticsTotalResponse(
                totalVoucherTypes,
                totalVoucherCount,
                totalExchangedVouchers,
                totalUsedVouchers,
                expiredVouchers,
                unexpiredVouchers,
                remainingVouchers
        );
    }

}

