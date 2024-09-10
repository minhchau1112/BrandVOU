package com.example.eventservice.model.event.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playerId", nullable = false)
    private UserEntity player;

    @ManyToOne
    @JoinColumn(name = "eventid", nullable = false)
    private EventEntity event;
}
