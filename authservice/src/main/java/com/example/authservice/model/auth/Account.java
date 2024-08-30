package com.example.authservice.model.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Account {

    private Long id;
    private String email;
    private String username;
    private String phoneNumber;
    private String role;
    private String status;
}
