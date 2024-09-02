package com.example.accountservice.model.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invalidate_token")
public class InvalidTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tokenId")
    private String tokenId;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
