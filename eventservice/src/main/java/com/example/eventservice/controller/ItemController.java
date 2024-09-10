package com.example.eventservice.controller;

import com.example.eventservice.model.item.dto.request.ItemCreateRequest;
import com.example.eventservice.model.item.dto.request.ItemUpdateRequest;
import com.example.eventservice.model.item.entity.ItemEntity;
import com.example.eventservice.service.ItemCreateService;
import com.example.eventservice.service.ItemDeleteService;
import com.example.eventservice.service.ItemReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/events/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemCreateService itemCreateService;
    private final ItemReadService itemReadService;
    private final ItemDeleteService itemDeleteService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Long> createItemForEvent(@ModelAttribute final ItemCreateRequest itemCreateRequest) {
        log.info("ItemController | createItemForEvent");
        final ItemEntity createdItem = itemCreateService.createItemForShakeGame(itemCreateRequest);

        if (createdItem != null) {
            return ResponseEntity.ok(createdItem.getId());
        }

        return ResponseEntity.ok((long) -1);
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<Page<ItemEntity>> getItemsByEvent(
            @PathVariable Long eventId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        log.info("ItemController | getItemsByEvent");

        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<ItemEntity> itemsPage = itemReadService.getItemsByEventId(eventId, pageable);

            return ResponseEntity.ok(itemsPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/brand/{brandId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<Page<ItemEntity>> getItemsByBrandId(
            @PathVariable Long brandId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String searchTerm) {
        log.info("ItemController | getItemsByBrandId");

        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<ItemEntity> itemsPage = itemReadService.findByBrandId(brandId, searchTerm, pageable);

            return ResponseEntity.ok(itemsPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{itemId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND', 'USER')")
    public ResponseEntity<ItemEntity> getItemByItemId(@PathVariable Long itemId) {
        log.info("ItemController | getItemByItemId");

        ItemEntity event = itemReadService.getItemById(itemId);

        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/all/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Void> deleteItemsOfEvent(@PathVariable Long eventId) {
        log.info("ItemController | deleteItem");

        itemDeleteService.deleteItemsOfEvent(eventId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        log.info("ItemController | deleteItem");

        itemDeleteService.deleteItem(itemId);

        return ResponseEntity.ok().build();
    }
}
