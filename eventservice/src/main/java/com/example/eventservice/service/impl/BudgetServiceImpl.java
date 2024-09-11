package com.example.eventservice.service.impl;

import com.example.eventservice.repository.BudgetRepository;
import com.example.eventservice.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public List<Map<String, Object>> getBudgetByBrandId(Long brandID) {
        List<Object[]> results = budgetRepository.findVoucherBudgetByBrandId(brandID);

        List<Map<String, Object>> budgetData = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> data = new HashMap<>();
            data.put("budget", result[0]);
            data.put("eventName", result[1]);
            budgetData.add(data);
        }
        return budgetData;
    }

    public List<Map<String, Object>> getParticipantCountByBrandId(Long brandID) {
        List<Object[]> results = budgetRepository.findParticipantCountByBrandId(brandID);

        List<Map<String, Object>> participantData = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> data = new HashMap<>();
            data.put("participantCount", result[0]);
            data.put("eventName", result[1]);
            participantData.add(data);
        }
        return participantData;
    }
}
