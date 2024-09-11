package com.example.accountservice.model.account.mapper;

import com.example.accountservice.model.account.dto.request.UpdateBrandRequest;
import com.example.accountservice.model.brand.entity.BrandEntity;
import com.example.accountservice.model.common.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UpdateBrandRequestToBrandEntityMapper extends BaseMapper<UpdateBrandRequest, BrandEntity> {

    @Named("mapForUpdating")
    default void mapForSaving(BrandEntity brandEntityToBeUpdate, UpdateBrandRequest updateBrandRequest) {
        brandEntityToBeUpdate.setName(updateBrandRequest.getName());
        brandEntityToBeUpdate.setAddress(updateBrandRequest.getAddress());
        brandEntityToBeUpdate.setField(updateBrandRequest.getField());
        brandEntityToBeUpdate.setGPS_lat(updateBrandRequest.getGpsLat());
        brandEntityToBeUpdate.setGPS_long(updateBrandRequest.getGpsLong());
        brandEntityToBeUpdate.setStatus(updateBrandRequest.getStatus());
    }

    static UpdateBrandRequestToBrandEntityMapper initialize() {
        return Mappers.getMapper(UpdateBrandRequestToBrandEntityMapper.class);
    }

}