package com.example.brand_backend.repository;

import com.example.brand_backend.model.Vouchers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Vouchers, Long> {
}
