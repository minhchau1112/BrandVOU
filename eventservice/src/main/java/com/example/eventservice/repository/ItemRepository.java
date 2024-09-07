package com.example.eventservice.repository;

import com.example.eventservice.model.item.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ItemRepository  extends JpaRepository<ItemEntity, Long> {
    @Transactional
    void deleteByEventId(Long eventId);
    @Query("SELECT ie FROM ItemEntity ie JOIN ie.event.brand b WHERE b.id = :brandId AND LOWER(ie.event.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<ItemEntity> findByBrandId(@Param("brandId") Long brandId, @Param("searchTerm") String searchTerm, Pageable pageable);
    Page<ItemEntity> findByEventId(Long eventId, Pageable pageable);
    boolean existsByNameAndEventId(final String name, final Long eventId);
}
