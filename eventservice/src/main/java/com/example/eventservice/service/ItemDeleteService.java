package com.example.eventservice.service;

public interface ItemDeleteService {
    void deleteItemsOfEvent(Long eventId);
    void deleteItem(Long itemId);
}
