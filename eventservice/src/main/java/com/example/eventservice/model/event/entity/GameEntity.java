package com.example.eventservice.model.event.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "games")
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String name;
    private String description;
    private String image;
    private String instructions;
    private Boolean isItemExchangeAllowed;
}
