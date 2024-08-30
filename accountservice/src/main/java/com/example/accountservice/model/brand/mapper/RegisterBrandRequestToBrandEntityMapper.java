package com.example.accountservice.model.brand.mapper;

import com.example.accountservice.model.account.entity.AccountEntity;
import com.example.accountservice.model.brand.dto.request.RegisterBrandRequest;
import com.example.accountservice.model.brand.entity.BrandEntity;
import com.example.accountservice.model.common.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
@Mapper
public interface RegisterBrandRequestToBrandEntityMapper extends BaseMapper<RegisterBrandRequest, BrandEntity> {
    @Named("mapForSaving")
    default BrandEntity mapForSaving(RegisterBrandRequest brandRegisterRequest, AccountEntity account) {

        return BrandEntity.builder()
                .account(account)
                .name(brandRegisterRequest.getName())
                .field(brandRegisterRequest.getField())
                .GPS_lat(brandRegisterRequest.getGpsLat())
                .GPS_long(brandRegisterRequest.getGpsLong())
                .address(brandRegisterRequest.getAddress())
                .status(brandRegisterRequest.getStatus())
                .build();
    }

    static RegisterBrandRequestToBrandEntityMapper initialize() {
        return Mappers.getMapper(RegisterBrandRequestToBrandEntityMapper.class);
    }
}
