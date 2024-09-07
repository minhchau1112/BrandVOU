package com.example.eventservice.model.event.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.event.dto.request.EventCreateRequest;
import com.example.eventservice.model.event.entity.BrandEntity;
import com.example.eventservice.model.event.entity.EventEntity;
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
                .gameType(eventCreateRequest.getGameType())
                .startTime(eventCreateRequest.getStartTime())
                .endTime(eventCreateRequest.getEndTime())
                .targetWord(eventCreateRequest.getTargetWord())
                .brand(brand)
                .build();
    }

    static EventCreateRequestToEventEntityMapper initialize() {
        return Mappers.getMapper(EventCreateRequestToEventEntityMapper.class);
    }
}
