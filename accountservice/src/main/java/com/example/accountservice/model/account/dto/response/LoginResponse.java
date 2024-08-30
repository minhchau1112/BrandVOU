package com.example.accountservice.model.account.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class LoginResponse {
  private String accessToken;
  private Long accessTokenExpiresAt;
  private String refreshToken;
  private Long id;
  private String username;
  private String role;
  private String status;

  public LoginResponse(String accessToken, Long accessTokenExpiresAt, String refreshToken, Long id, String username, String role, String status) {
    this.accessToken = accessToken;
    this.accessTokenExpiresAt = accessTokenExpiresAt;
    this.refreshToken = refreshToken;
    this.id = id;
    this.username = username;
    this.role = role;
    this.status = status;
  }
}
