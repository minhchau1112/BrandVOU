package com.example.eventservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface VoucherDeleteService {
    ResponseEntity<Void> deleteVoucher(@PathVariable Long voucherId);
}
