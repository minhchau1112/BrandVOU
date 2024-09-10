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

    @Query("SELECT e, " +
            "CASE WHEN n.id IS NOT NULL THEN true ELSE false END AS notificationSent " +
            "FROM EventEntity e " +
            "LEFT JOIN NotificationEntity n ON e.id = n.event.id AND n.player.id = :playerId " +
            "WHERE e.startTime >= CURRENT_TIMESTAMP " +
            "OR (e.startTime <= CURRENT_TIMESTAMP AND e.endTime >= CURRENT_TIMESTAMP)" +
            "AND LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            "ORDER BY e.startTime ASC, e.name ASC")
    Page<Object[]> findEventsWithNotificationStatus(@Param("playerId") Long playerId, @Param("searchTerm") String searchTerm, Pageable pageable);
}
