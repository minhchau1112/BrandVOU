package com.example.accountservice.controller;

import com.example.accountservice.model.account.Token;
import com.example.accountservice.model.account.dto.request.LoginRequest;
import com.example.accountservice.model.account.dto.response.LoginResponse;
import com.example.accountservice.model.account.entity.AccountEntity;
import com.example.accountservice.model.brand.dto.request.RegisterBrandRequest;
import com.example.accountservice.model.brand.entity.BrandEntity;
import com.example.accountservice.service.BrandLoginService;
import com.example.accountservice.service.RegisterBrandService;
import com.example.accountservice.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class BrandController {
    private final RegisterBrandService registerBrandService;
    private final BrandLoginService brandLoginService;
    private final UserLoginService userLoginService;
    @PostMapping("/register-brand")
    public ResponseEntity<?> registerBrand(@RequestBody @Validated final RegisterBrandRequest registerBrandRequest) {
        log.info("BrandController | registerBrand");
        ResponseEntity<?> brand = registerBrandService.registerBrand(registerBrandRequest);
        return brand;
    }

    @PostMapping("/login-brand")
    public ResponseEntity<?> loginBrand(@RequestBody @Validated final LoginRequest loginBrandRequest) {
        log.info("BrandController | loginBrand");
        log.info("BrandController | validateToken");
        final Token token = userLoginService.login(loginBrandRequest);

        final AccountEntity account = userLoginService.loadUserByUsername(loginBrandRequest.getUsername());

        final BrandEntity brand = brandLoginService.getBrandInfo(account.getId());

        LoginResponse response = new LoginResponse(
                token.getAccessToken(),
                token.getAccessTokenExpiresAt(),
                token.getRefreshToken(),
                brand.getId(),
                brand.getName(),
                account.getRole(),
                account.getStatus());

        return ResponseEntity.ok(response);
    }
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<BrandEntity> getBrandInfo(@RequestParam Long accountId) {
        log.info("BrandController | getBrandInfo");
        BrandEntity brand = brandLoginService.getBrandInfo(accountId);
        return ResponseEntity.ok(brand);
    }

    @GetMapping("/brands")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<BrandEntity> findBrandById(@RequestParam Long brandId) {
        log.info("BrandController | findBrandById");
        BrandEntity brand = brandLoginService.findBrandById(brandId);
        return ResponseEntity.ok(brand);
    }
}
