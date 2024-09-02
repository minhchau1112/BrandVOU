package com.example.accountservice.repository;

import com.example.accountservice.model.brand.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
    boolean existsBrandEntityByName(final String name);
    Optional<BrandEntity> findBrandEntityByName(final String name);
    Optional<BrandEntity> findBrandEntityByAccountId(final Long accountid);
}
