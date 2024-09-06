package com.example.eventservice.repository;

import com.example.eventservice.model.voucher.entity.VoucherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query("SELECT v.code, v.count AS totalVouchers, " +
            "COALESCE(SUM(vt.quantity), 0) AS exchangedVouchers, " +
            "COALESCE(SUM(vt.usedQuantity), 0) AS usedVouchers, " +
            "(v.count - COALESCE(SUM(vt.quantity), 0)) AS remainingVouchers " +
            "FROM VoucherEntity v " +
            "LEFT JOIN VoucherTransactionEntity vt ON v.id = vt.voucher.id " +
            "WHERE v.event.id = :eventId " +
            "GROUP BY v.code, v.count")
    List<Object[]> getVoucherStatsDetailByEvent(@Param("eventId") Long eventId);

    @Query("SELECT " +
            "COUNT(DISTINCT v.code) AS totalVoucherTypes, " +
            "SUM(v.count) AS totalVoucherCount, " +
            "COALESCE(SUM(vt.quantity), 0) AS totalExchangedVouchers, " +
            "COALESCE(SUM(vt.usedQuantity), 0) AS totalUsedVouchers, " +
            "SUM(CASE WHEN v.expirationDate < CURRENT_TIMESTAMP THEN 1 ELSE 0 END) AS expiredVouchers, " +
            "SUM(CASE WHEN v.expirationDate >= CURRENT_TIMESTAMP THEN 1 ELSE 0 END) AS unexpiredVouchers, " +
            "(SUM(v.count) - COALESCE(SUM(vt.quantity), 0)) AS remainingVouchers " +
            "FROM VoucherEntity v " +
            "LEFT JOIN VoucherTransactionEntity vt ON v.id = vt.voucher.id " +
            "WHERE v.event.id = :eventId")
    Object[] getVoucherStatsGeneralByEvent(@Param("eventId") Long eventId);
}