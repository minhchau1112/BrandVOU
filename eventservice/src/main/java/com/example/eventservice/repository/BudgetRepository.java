package com.example.eventservice.repository;

import com.example.eventservice.model.voucher.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<VoucherEntity, Long> {
    @Query("SELECT SUM(v.value * v.count) AS totalBudget, e.name AS eventName " +
            "FROM VoucherEntity v JOIN v.event e " +
            "WHERE e.brand.id = :brandID " +
            "GROUP BY e.name")
    List<Object[]> findVoucherBudgetByBrandId(@Param("brandID") Long brandID);

    @Query("SELECT SUM(v.count) AS participantCount, e.name AS eventName " +
            "FROM VoucherEntity v JOIN v.event e " +
            "WHERE e.brand.id = :brandID " +
            "GROUP BY e.name")
    List<Object[]> findParticipantCountByBrandId(@Param("brandID") Long brandID);
}
