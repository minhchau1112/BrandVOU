package com.example.eventservice.model.item.entity;

import com.example.eventservice.model.event.entity.EventEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    private String type;
    @ManyToOne
    @JoinColumn(name = "eventid", nullable = false)
    private EventEntity event;
}
