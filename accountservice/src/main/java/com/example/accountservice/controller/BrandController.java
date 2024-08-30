package com.example.accountservice.controller;

import com.example.accountservice.model.brand.dto.request.RegisterBrandRequest;
import com.example.accountservice.model.brand.entity.BrandEntity;
import com.example.accountservice.service.BrandLoginService;
import com.example.accountservice.service.RegisterBrandService;
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

    @PostMapping("/register-brand")
    public ResponseEntity<Void> registerBrand(@RequestBody @Validated final RegisterBrandRequest registerBrandRequest) {
        log.info("BrandController | registerBrand");
        registerBrandService.registerBrand(registerBrandRequest);
        return ResponseEntity.ok().build();
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
