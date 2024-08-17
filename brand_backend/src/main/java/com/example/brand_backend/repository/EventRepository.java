package com.example.brand_backend.repository;

import com.example.brand_backend.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Events, Long> {
}
