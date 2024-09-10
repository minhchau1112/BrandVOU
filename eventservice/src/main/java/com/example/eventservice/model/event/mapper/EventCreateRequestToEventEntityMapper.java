package com.example.eventservice.model.event.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.event.dto.request.EventCreateRequest;
import com.example.eventservice.model.event.entity.BrandEntity;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.voucher.dto.request.VoucherCreateRequest;
import com.example.eventservice.model.voucher.entity.VoucherEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface EventCreateRequestToEventEntityMapper extends BaseMapper<EventCreateRequest, EventEntity> {

    @Named("mapForSaving")
    default EventEntity mapForSaving(EventCreateRequest eventCreateRequest, BrandEntity brand, String imageUrl) {

        return EventEntity.builder()
                .name(eventCreateRequest.getName())
                .image(imageUrl)
                .voucherCount(eventCreateRequest.getVoucherCount())
                .startTime(eventCreateRequest.getStartTime())
                .endTime(eventCreateRequest.getEndTime())
                .targetWord(eventCreateRequest.getTargetWord())
                .brand(brand)
                .build();
    }

    @Named("mapMultipartFileToString")
    default String mapMultipartFileToString(MultipartFile file) {
        return file != null ? file.getOriginalFilename() : null;
    }

    @Override
    @Mapping(target = "image", source = "image", qualifiedByName = "mapMultipartFileToString")
    EventEntity map(EventCreateRequest source);

    static EventCreateRequestToEventEntityMapper initialize() {
        return Mappers.getMapper(EventCreateRequestToEventEntityMapper.class);
    }
}
