package com.example.eventservice.repository;

import com.example.eventservice.model.event.entity.EventGamesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EventGamesRepository extends JpaRepository<EventGamesEntity, Long> {
    @Transactional
    void deleteAllByEventId(Long eventId);
    @Query("SELECT g.name FROM EventGamesEntity eg JOIN eg.game g WHERE eg.event.id = :eventId")
    List<String> findGameNamesByEventId(@Param("eventId") Long eventId);
}
