package com.example.authservice.model.auth.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
  private String accessToken;
  private Long accessTokenExpiresAt;
  private String refreshToken;
  private Long id;
  private String username;
  private String role;
  private String status;
}
