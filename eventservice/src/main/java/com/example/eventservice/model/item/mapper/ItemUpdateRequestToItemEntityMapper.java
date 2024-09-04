package com.example.eventservice.model.item.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.event.entity.EventEntity;
import com.example.eventservice.model.item.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemUpdateRequestToItemEntityMapper extends BaseMapper<String, EventEntity> {
    @Named("mapForUpdating")
    default void mapForUpdating(ItemEntity itemEntityToBeUpdate, String imageUrl) {
        if (imageUrl != null) {
            itemEntityToBeUpdate.setImage(imageUrl);
        }
    }

    static ItemUpdateRequestToItemEntityMapper initialize() {
        return Mappers.getMapper(ItemUpdateRequestToItemEntityMapper.class);
    }
}
