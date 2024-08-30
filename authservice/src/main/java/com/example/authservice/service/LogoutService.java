package com.example.authservice.service;

import com.example.authservice.model.auth.dto.request.TokenInvalidateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface LogoutService {
    ResponseEntity<Void> logout(@RequestBody @Valid final TokenInvalidateRequest tokenInvalidateRequest);

}
