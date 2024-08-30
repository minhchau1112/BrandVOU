package com.example.accountservice.service.impl;

import com.example.accountservice.exception.PasswordNotValidException;
import com.example.accountservice.exception.UserNotFoundException;
import com.example.accountservice.model.account.Token;
import com.example.accountservice.model.account.dto.request.LoginRequest;
import com.example.accountservice.model.account.entity.AccountEntity;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.service.TokenService;
import com.example.accountservice.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    @Override
    public Token login(LoginRequest loginRequest) {

        final AccountEntity AccountEntityFromDB = loadUserByUsername(loginRequest.getUsername());

        if (Boolean.FALSE.equals(passwordEncoder.matches(
                loginRequest.getPassword(), AccountEntityFromDB.getPassword()))) {
            throw new PasswordNotValidException("Password is not valid!");
        }

        return tokenService.generateToken(AccountEntityFromDB.getClaims());

    }
    @Override
    public AccountEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        final AccountEntity AccountEntityFromDB = accountRepository
                .findAccountEntityByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("Can't find with given username: "
                                + username)
                );

        return AccountEntityFromDB;
    }
}
