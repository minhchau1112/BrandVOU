package com.example.eventservice.model.voucher.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.voucher.dto.request.VoucherCreateRequest;
import com.example.eventservice.model.voucher.dto.request.VoucherUpdateRequest;
import com.example.eventservice.model.voucher.entity.VoucherEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface VoucherCreateRequestToVoucherEntityMapper extends BaseMapper<VoucherCreateRequest, VoucherEntity> {

    @Named("mapForSaving")
    default VoucherEntity mapForSaving(VoucherCreateRequest voucherCreateRequest, EventEntity eventEntity, String QRCodeUrl, String imageUrl) {

        return VoucherEntity.builder()
                .code(voucherCreateRequest.getCode())
                .image(imageUrl)
                .QRCode(QRCodeUrl)
                .description(voucherCreateRequest.getDescription())
                .count(eventEntity.getVoucherCount())
                .expirationDate(voucherCreateRequest.getExpirationDate())
                .value(voucherCreateRequest.getValue())
                .status(voucherCreateRequest.getStatus())
                .event(eventEntity)
                .build();
    }

    @Named("mapMultipartFileToString")
    default String mapMultipartFileToString(MultipartFile file) {
        return file != null ? file.getOriginalFilename() : null;
    }

    @Override
    @Mapping(target = "QRCode", source = "QRCode", qualifiedByName = "mapMultipartFileToString")
    @Mapping(target = "image", source = "image", qualifiedByName = "mapMultipartFileToString")
    VoucherEntity map(VoucherCreateRequest source);

    static VoucherCreateRequestToVoucherEntityMapper initialize() {
        return Mappers.getMapper(VoucherCreateRequestToVoucherEntityMapper.class);
    }
}
