package com.example.reportservice.repository;

import com.example.reportservice.model.statistics.dto.StatisticsResponse;
import com.example.reportservice.model.statistics.entity.StatisticsEntity;
import com.example.reportservice.model.statistics.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<VoucherEntity, Long> {
    @Query("SELECT v.value * v.count AS profit, e.name AS eventName " +
            "FROM VoucherEntity v JOIN v.event e " + // Sử dụng alias `v.event` thay vì `EventEntity e ON v.event.id = e.id`
            "WHERE e.brand.id = :brandID")
    List<Object[]> findVoucherProfitsByBrandId(@Param("brandID") Long brandID);
}

