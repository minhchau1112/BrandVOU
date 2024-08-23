package com.example.brand_backend.repository;

import com.example.brand_backend.model.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Events, Long> {
    Page<Events> findByBrandId(Long brandID, Pageable pageable);
    boolean existsByNameAndBrandId(String name, Long brandId);
}
