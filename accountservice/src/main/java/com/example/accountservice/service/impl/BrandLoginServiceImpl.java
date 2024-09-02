package com.example.accountservice.service.impl;

import com.example.accountservice.exception.BrandNotFoundException;
import com.example.accountservice.model.brand.entity.BrandEntity;
import com.example.accountservice.repository.BrandRepository;
import com.example.accountservice.service.BrandLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandLoginServiceImpl implements BrandLoginService {
    private final BrandRepository brandRepository;
    @Override
    public BrandEntity getBrandInfo(Long accountId) {
        final Optional<BrandEntity> brand = brandRepository.findBrandEntityByAccountId(accountId);
        if (brand.isEmpty()) {
            throw new BrandNotFoundException("Brand with accountId= " + accountId + " not found!");
        }
        return brand.get();
    }

    @Override
    public BrandEntity findBrandById(Long brandId) {
        final Optional<BrandEntity> brand = brandRepository.findById(brandId);
        if (brand.isEmpty()) {
            throw new BrandNotFoundException("Brand with brandId= " + brandId + " not found!");
        }
        return brand.get();
    }
}
