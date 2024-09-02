package com.example.eventservice.repository;

import com.example.eventservice.model.voucher.entity.VoucherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {
    @Query("SELECT v FROM VoucherEntity v JOIN v.event e WHERE e.brand.id = :brandID AND LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<VoucherEntity> findVouchersByBrandId(
            @Param("brandID") Long brandID,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
    @Query("SELECT v from VoucherEntity v WHERE v.event.id = :eventID AND LOWER(v.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<VoucherEntity> findVouchersByEventId(
            @Param("eventID") Long eventID,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
    boolean existsByCodeAndEventId(String code, Long eventId);
    VoucherEntity findByCodeAndEventId(String code, Long eventId);
}