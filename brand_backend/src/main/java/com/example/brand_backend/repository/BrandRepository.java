package com.example.brand_backend.repository;

import com.example.brand_backend.model.Brands;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brands, Long> {
}
