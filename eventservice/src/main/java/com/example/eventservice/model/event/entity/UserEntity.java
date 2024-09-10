package com.example.eventservice.model.event.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clerk_id", length = 255, nullable = false)
    private String clerkId;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "dob", length = 255)
    private String dob;

    @Column(name = "email", length = 45)
    private String email;

    @Column(name = "gender", length = 45)
    private String gender;

    @Column(name = "name" , length = 255)
    private String name;

    @Column(name = "phone_number", length = 45)
    private String phoneNumber;

    @Column(name = "role", length = 45)
    private String role;

    @Column(name = "username", length = 45)
    private String username;

}
