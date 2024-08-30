package com.example.eventservice.repository;

import com.example.eventservice.model.event.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    Page<EventEntity> findByBrandId(Long brandId, Pageable pageable);
    boolean existsByNameAndBrandId(String name, Long brandId);
    EventEntity findByNameAndBrandId(String name, Long brandId);
}
