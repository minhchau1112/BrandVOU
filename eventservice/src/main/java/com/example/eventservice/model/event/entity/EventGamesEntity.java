package com.example.eventservice.model.event.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "eventgames")
public class EventGamesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eventid", nullable = false)
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "gameid", nullable = false)
    private GameEntity game;
}
