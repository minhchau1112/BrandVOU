package com.example.eventservice.repository;

import com.example.eventservice.model.event.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    Page<EventEntity> findByBrandId(Long brandId, Pageable pageable);
    @Query("SELECT e FROM EventEntity e WHERE e.brand.id = :brandId AND LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<EventEntity> findByBrandIdAndSearch(
            @Param("brandId") Long brandId,
            @Param("searchTerm") String searchTerm,
            Pageable pageable);
    List<EventEntity> findAllByBrandId(Long brandId);
    boolean existsByNameAndBrandId(String name, Long brandId);
    EventEntity findByNameAndBrandId(String name, Long brandId);

    @Query("SELECT e FROM EventEntity e WHERE e.brand.id = :brandId AND e.targetWord IS NOT NULL AND e.targetWord <> ''")
    List<EventEntity> findEventsOfBrandHaveTargetWord(Long brandId);
}
