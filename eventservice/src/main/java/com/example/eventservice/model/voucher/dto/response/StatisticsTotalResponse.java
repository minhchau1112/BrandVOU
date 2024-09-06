package com.example.eventservice.model.voucher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class StatisticsTotalResponse {
    Long totalVoucherTypes;
    Long totalVoucherCount;
    Long totalExchangedVouchers;
    Long totalUsedVouchers;
    Long expiredVouchers;
    Long unexpiredVouchers;
    Long remainingVouchers;
}
