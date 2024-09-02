package com.example.eventservice.model.voucher.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.event.mapper.EventUpdateRequestToEventEntityMapper;
import com.example.eventservice.model.voucher.dto.request.VoucherCreateRequest;
import com.example.eventservice.model.voucher.dto.request.VoucherUpdateRequest;
import com.example.eventservice.model.voucher.entity.VoucherEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface VoucherUpdateRequestToVoucherEntityMapper extends BaseMapper<VoucherUpdateRequest, VoucherEntity> {

    @Named("mapForUpdating")
    default void mapForUpdating(VoucherEntity voucherEntityToBeUpdate, EventEntity eventEntity, VoucherUpdateRequest voucherUpdateRequest, String QRCodeUrl, String imageUrl) {
        voucherEntityToBeUpdate.setCode(voucherUpdateRequest.getCode());
        voucherEntityToBeUpdate.setImage(imageUrl);
        voucherEntityToBeUpdate.setQRCode(QRCodeUrl);
        voucherEntityToBeUpdate.setDescription(voucherUpdateRequest.getDescription());
        voucherEntityToBeUpdate.setCount(voucherUpdateRequest.getCount());
        voucherEntityToBeUpdate.setExpirationDate(voucherUpdateRequest.getExpirationDate());
        voucherEntityToBeUpdate.setStatus(voucherUpdateRequest.getStatus());
        voucherEntityToBeUpdate.setValue(voucherUpdateRequest.getValue());
        voucherEntityToBeUpdate.setEvent(eventEntity);
    }
    @Named("mapMultipartFileToString")
    default String mapMultipartFileToString(MultipartFile file) {
        return file != null ? file.getOriginalFilename() : null;
    }

    @Override
    @Mapping(target = "QRCode", source = "QRCode", qualifiedByName = "mapMultipartFileToString")
    @Mapping(target = "image", source = "image", qualifiedByName = "mapMultipartFileToString") // Explicit mapping for the image field
    VoucherEntity map(VoucherUpdateRequest source);

    static VoucherUpdateRequestToVoucherEntityMapper initialize() {
        return Mappers.getMapper(VoucherUpdateRequestToVoucherEntityMapper.class);
    }

}