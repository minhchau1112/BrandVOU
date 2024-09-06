package com.example.eventservice.controller;

import com.example.eventservice.model.voucher.dto.request.VoucherCreateRequest;
import com.example.eventservice.model.voucher.dto.request.VoucherUpdateRequest;
import com.example.eventservice.model.voucher.dto.response.StatisticsByVoucherTypeResponse;
import com.example.eventservice.model.voucher.dto.response.StatisticsTotalResponse;
import com.example.eventservice.model.voucher.entity.VoucherEntity;
import com.example.eventservice.repository.VoucherRepository;
import com.example.eventservice.service.VoucherCreateService;
import com.example.eventservice.service.VoucherDeleteService;
import com.example.eventservice.service.VoucherReadService;
import com.example.eventservice.service.VoucherUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/events/vouchers")
@RequiredArgsConstructor
@Validated
public class VoucherController {
    private final VoucherCreateService voucherCreateService;
    private final VoucherReadService voucherReadService;
    private final VoucherUpdateService voucherUpdateService;
    private final VoucherDeleteService voucherDeleteService;
    private final VoucherRepository voucherRepository;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Long> createVoucherForEvent(@ModelAttribute final VoucherCreateRequest voucherCreateRequest) {
        log.info("VoucherController | createVoucherForEvent");

        final VoucherEntity createdVoucher = voucherCreateService.createVoucherForEvent(voucherCreateRequest);

        if (createdVoucher != null) {
            return ResponseEntity.ok(createdVoucher.getId());
        }

        return ResponseEntity.ok((long) -1);
    }

    @GetMapping("/brand/{brandId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<Page<VoucherEntity>> getVouchersByBrand(
            @PathVariable Long brandId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String searchTerm) {
        log.info("VoucherController | getVouchersByBrand");

        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<VoucherEntity> vouchersPage = voucherReadService.getVouchersByBrandId(brandId, searchTerm, pageable);

            return ResponseEntity.ok(vouchersPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{voucherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<VoucherEntity> getVoucherByVoucherId(@PathVariable Long voucherId) {
        log.info("VoucherController | getVoucherByVoucherId");

        VoucherEntity voucher = voucherReadService.getVoucherById(voucherId);
        return ResponseEntity.ok(voucher);
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<Page<VoucherEntity>> getVoucherByEventId(
            @PathVariable Long eventId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String searchTerm) {
        log.info("VoucherController | getVoucherByEventId");

        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<VoucherEntity> vouchersPage = voucherReadService.findVouchersByEventId(eventId, searchTerm, pageable);

            return ResponseEntity.ok(vouchersPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{voucherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<VoucherEntity> updateVoucher(@PathVariable Long voucherId, @ModelAttribute final VoucherUpdateRequest voucherUpdateRequest) {
        log.info("VoucherController | updateVoucher");

        VoucherEntity voucherEntity = voucherUpdateService.updateVoucherById(voucherId, voucherUpdateRequest);

        return ResponseEntity.ok(voucherEntity);
    }

    @DeleteMapping("/{voucherId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long voucherId) {
        log.info("VoucherController | deleteVoucher");

        return voucherDeleteService.deleteVoucher(voucherId);
    }

    @GetMapping("/statistics-detail/event/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<List<StatisticsByVoucherTypeResponse>> getVoucherStatsDetailByEvent(@PathVariable Long eventId) {
        log.info("VoucherController | getVoucherStatsDetailByEvent");

        List<StatisticsByVoucherTypeResponse> statistics = voucherReadService.getVoucherStatsDetailByEvent(eventId);

        return ResponseEntity.ok(statistics);
    }
    @GetMapping("/statistics-general/event/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<StatisticsTotalResponse> getVoucherStatsGeneralByEvent(@PathVariable Long eventId) {
        log.info("VoucherController | getVoucherStatsByEvent");

        StatisticsTotalResponse statistics = voucherReadService.getVoucherStatsGeneralByEvent(eventId);

        return ResponseEntity.ok(statistics);
    }
}
