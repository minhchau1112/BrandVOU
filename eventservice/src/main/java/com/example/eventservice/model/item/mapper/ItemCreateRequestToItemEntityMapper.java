package com.example.eventservice.model.item.mapper;

import com.example.eventservice.model.common.mapper.BaseMapper;
import com.example.eventservice.model.item.dto.request.ItemCreateRequest;
import com.example.eventservice.model.item.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemCreateRequestToItemEntityMapper extends BaseMapper<ItemCreateRequest, ItemEntity> {
    @Named("mapForSaving")
    default ItemEntity mapForSaving(final ItemCreateRequest itemCreateRequest) {

        return ItemEntity.builder()
                .name(itemCreateRequest.getName())
                .build();
    }

    static ItemCreateRequestToItemEntityMapper initialize() {
        return Mappers.getMapper(ItemCreateRequestToItemEntityMapper.class);
    }
}
