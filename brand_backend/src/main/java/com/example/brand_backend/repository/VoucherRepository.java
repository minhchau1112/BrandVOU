package com.example.brand_backend.repository;

import com.example.brand_backend.model.Vouchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Vouchers, Long> {
    @Query("SELECT v FROM Vouchers v JOIN v.event e WHERE e.brand.id = :brandID")
    Page<Vouchers> findVouchersByBrandId(@Param("brandID") Long brandID, Pageable pageable);
    Optional<Vouchers> findById(Long id);
    Page<Vouchers> findVouchersByEventId(Long eventID, Pageable pageable);
    boolean existsByCodeAndEventId(String code, Long eventId);
}
