package com.example.eventservice.model.voucher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class StatisticsByVoucherTypeResponse {
    String code;
    Integer totalVouchers;
    Long exchangedVouchers;
    Long usedVouchers;
    Long remainingVouchers;
}
