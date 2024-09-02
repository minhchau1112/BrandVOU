package com.example.accountservice.model.account.entity;

import com.example.accountservice.model.account.enums.TokenClaims;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String role;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Map<String, Object> getClaims() {

        final Map<String, Object> claims = new HashMap<>();

        claims.put(TokenClaims.USER_ID.getValue(), this.id);
        claims.put(TokenClaims.USER_STATUS.getValue(), this.status);
        claims.put(TokenClaims.ROLE.getValue(), this.role);
        claims.put(TokenClaims.USER_USER_NAME.getValue(), this.username);
        claims.put(TokenClaims.USER_EMAIL.getValue(), this.email);
        claims.put(TokenClaims.USER_PHONE_NUMBER.getValue(), this.phoneNumber);

        return claims;

    }

}
