package com.example.accountservice.service;

import com.example.accountservice.model.brand.entity.BrandEntity;

public interface BrandLoginService {
    BrandEntity getBrandInfo(final Long accountId);
}
