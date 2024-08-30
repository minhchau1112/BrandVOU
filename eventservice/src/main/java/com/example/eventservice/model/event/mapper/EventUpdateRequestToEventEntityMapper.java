package com.example.eventservice.model.event.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.event.dto.request.EventUpdateRequest;
import com.example.eventservice.model.event.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventUpdateRequestToEventEntityMapper extends BaseMapper<EventUpdateRequest, EventEntity> {

    @Named("mapForUpdating")
    default void mapForUpdating(EventEntity eventEntityToBeUpdate, EventUpdateRequest eventUpdateRequest) {
        eventEntityToBeUpdate.setName(eventUpdateRequest.getName());
        eventEntityToBeUpdate.setImage(eventUpdateRequest.getImage());
        eventEntityToBeUpdate.setVoucherCount(eventUpdateRequest.getVoucherCount());
        eventEntityToBeUpdate.setGameType(eventUpdateRequest.getGameType());
        eventEntityToBeUpdate.setStartTime(eventUpdateRequest.getStartTime());
        eventEntityToBeUpdate.setEndTime(eventUpdateRequest.getEndTime());
    }

    static EventUpdateRequestToEventEntityMapper initialize() {
        return Mappers.getMapper(EventUpdateRequestToEventEntityMapper.class);
    }

}
