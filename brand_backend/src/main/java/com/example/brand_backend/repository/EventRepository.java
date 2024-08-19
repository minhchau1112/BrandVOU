package com.example.brand_backend.repository;

import com.example.brand_backend.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Events, Long> {
    List<Events> findByBrandId(Long brandID);
}
