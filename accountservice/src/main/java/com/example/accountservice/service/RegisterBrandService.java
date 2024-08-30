package com.example.accountservice.service;

import com.example.accountservice.model.brand.dto.request.RegisterBrandRequest;
import com.example.accountservice.model.brand.entity.BrandEntity;

public interface RegisterBrandService {
    BrandEntity registerBrand(final RegisterBrandRequest registerRequest);
}
