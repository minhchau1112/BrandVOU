package com.example.accountservice.service.impl;

import com.example.accountservice.exception.UserNotFoundException;
import com.example.accountservice.exception.UserStatusNotValidException;
import com.example.accountservice.model.account.Token;
import com.example.accountservice.model.account.dto.request.TokenRefreshRequest;
import com.example.accountservice.model.account.entity.AccountEntity;
import com.example.accountservice.model.account.enums.TokenClaims;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.service.RefreshTokenService;
import com.example.accountservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final AccountRepository accountRepository;

    private final TokenService tokenService;

    @Override
    public Token refreshToken(TokenRefreshRequest tokenRefreshRequest) {

        tokenService.verifyAndValidate(tokenRefreshRequest.getRefreshToken());

        final Long adminId = Long.valueOf(tokenService
                .getPayload(tokenRefreshRequest.getRefreshToken())
                .get(TokenClaims.USER_ID.getValue()).toString());

        final AccountEntity AccountEntityFromDB = accountRepository
                .findById(adminId)
                .orElseThrow(UserNotFoundException::new);

        this.validateUserStatus(AccountEntityFromDB);

        System.out.println("TOKEN: " + tokenService.generateToken(
                AccountEntityFromDB.getClaims(),
                tokenRefreshRequest.getRefreshToken()
        ));
        return tokenService.generateToken(
                AccountEntityFromDB.getClaims(),
                tokenRefreshRequest.getRefreshToken()
        );

    }

    private void validateUserStatus(final AccountEntity AccountEntity) {
        if (!("ACTIVE".equals(AccountEntity.getStatus()))) {
            throw new UserStatusNotValidException("UserStatus = " + AccountEntity.getStatus());
        }
    }

}
