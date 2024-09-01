package com.example.accountservice.service;

import com.example.accountservice.model.account.dto.request.RegisterRequest;
import com.example.accountservice.model.account.entity.AccountEntity;
import org.springframework.http.ResponseEntity;

public interface RegisterService {

    ResponseEntity<?> registerUser(final RegisterRequest registerRequest);

}
