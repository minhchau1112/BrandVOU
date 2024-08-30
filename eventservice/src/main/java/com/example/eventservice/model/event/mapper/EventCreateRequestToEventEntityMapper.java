package com.example.eventservice.model.event.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.event.dto.request.EventCreateRequest;
import com.example.eventservice.model.event.entity.BrandEntity;
import com.example.eventservice.model.event.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventCreateRequestToEventEntityMapper extends BaseMapper<EventCreateRequest, EventEntity> {

    @Named("mapForSaving")
    default EventEntity mapForSaving(EventCreateRequest eventCreateRequest, BrandEntity brand) {

        return EventEntity.builder()
                .name(eventCreateRequest.getName())
                .image(eventCreateRequest.getImage())
                .voucherCount(eventCreateRequest.getVoucherCount())
                .gameType(eventCreateRequest.getGameType())
                .startTime(eventCreateRequest.getStartTime())
                .endTime(eventCreateRequest.getEndTime())
                .brand(brand)
                .build();
    }

    static EventCreateRequestToEventEntityMapper initialize() {
        return Mappers.getMapper(EventCreateRequestToEventEntityMapper.class);
    }

}
