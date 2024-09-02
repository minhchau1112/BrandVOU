package com.example.accountservice.service.impl;

import com.example.accountservice.exception.UserAlreadyExistException;
import com.example.accountservice.model.account.dto.request.RegisterRequest;
import com.example.accountservice.model.account.entity.AccountEntity;
import com.example.accountservice.model.account.mapper.RegisterRequestToAccountEntityMapper;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final AccountRepository accountRepository;
    private final RegisterRequestToAccountEntityMapper registerRequestToAccountEntityMapper = RegisterRequestToAccountEntityMapper.initialize();

    private final PasswordEncoder passwordEncoder;
    @Override
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {

        if (accountRepository.existsAccountEntityByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .ok(new UserAlreadyExistException("The email is already used for another account"));
        }

        if (accountRepository.existsAccountEntityByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .ok(new UserAlreadyExistException("The username is already used for another account"));
        }

        final AccountEntity AccountEntityToBeSave = registerRequestToAccountEntityMapper.mapForSaving(registerRequest);

        AccountEntityToBeSave.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        AccountEntityToBeSave.setCreatedAt(LocalDateTime.now());

        AccountEntity savedAccountEntity = accountRepository.save(AccountEntityToBeSave);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccountEntity);

    }

}
