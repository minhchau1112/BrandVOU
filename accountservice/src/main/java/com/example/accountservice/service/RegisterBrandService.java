package com.example.accountservice.service;

import com.example.accountservice.model.brand.dto.request.RegisterBrandRequest;
import com.example.accountservice.model.brand.entity.BrandEntity;
import org.springframework.http.ResponseEntity;

public interface RegisterBrandService {
    ResponseEntity<?> registerBrand(final RegisterBrandRequest registerRequest);
}
