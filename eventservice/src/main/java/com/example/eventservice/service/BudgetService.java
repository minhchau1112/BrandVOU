package com.example.eventservice.service;

import java.util.List;
import java.util.Map;

public interface BudgetService {
    List<Map<String, Object>> getBudgetByBrandId(Long brandID);
    List<Map<String, Object>> getParticipantCountByBrandId(Long brandID);
}
