package com.example.brand_backend.repository;

import com.example.brand_backend.model.Vouchers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Vouchers, Long> {
    @Query("SELECT v, e FROM Vouchers v JOIN Events e ON v.event.id = e.id WHERE e.brand.id = :brandID")
    List<Vouchers> findVouchersByBrandId(@Param("brandID") Long brandID);
    Optional<Vouchers> findById(Long id);
}
