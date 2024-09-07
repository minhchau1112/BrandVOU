package com.example.eventservice.model.event.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.event.dto.request.EventCreateRequest;
import com.example.eventservice.model.event.dto.request.EventUpdateRequest;
import com.example.eventservice.model.event.entity.BrandEntity;
import com.example.eventservice.model.event.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface EventUpdateRequestToEventEntityMapper extends BaseMapper<EventUpdateRequest, EventEntity> {

    @Named("mapForUpdating")
    default void mapForUpdating(EventEntity eventEntityToBeUpdate, EventUpdateRequest eventUpdateRequest, String imageUrl) {
        eventEntityToBeUpdate.setName(eventUpdateRequest.getName());
        if (imageUrl != null) {
            eventEntityToBeUpdate.setImage(imageUrl);
        }
        eventEntityToBeUpdate.setVoucherCount(eventUpdateRequest.getVoucherCount());
        eventEntityToBeUpdate.setStartTime(eventUpdateRequest.getStartTime());
        eventEntityToBeUpdate.setEndTime(eventUpdateRequest.getEndTime());
        eventEntityToBeUpdate.setTargetWord(eventUpdateRequest.getTargetWord());
    }

    static EventUpdateRequestToEventEntityMapper initialize() {
        return Mappers.getMapper(EventUpdateRequestToEventEntityMapper.class);
    }

}
