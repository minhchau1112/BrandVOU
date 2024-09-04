package com.example.eventservice.repository;

import com.example.eventservice.model.event.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, Long> {

}
