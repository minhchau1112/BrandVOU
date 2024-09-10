package com.example.eventservice.repository;

import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.event.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query("SELECT e FROM EventEntity e JOIN NotificationEntity n ON e.id = n.event.id WHERE n.player.id = :playerId AND LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY e.startTime DESC , e.name ASC")
    Page<EventEntity> findFavouriteEvent(@Param("playerId") Long playerId, @Param("searchTerm") String searchTerm, Pageable pageable);
}
