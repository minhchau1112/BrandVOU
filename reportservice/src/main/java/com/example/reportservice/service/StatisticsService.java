package com.example.reportservice.service;

import com.example.reportservice.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    public List<Map<String, Object>> getProfitByBrandId(Long brandID) {
        List<Object[]> results = statisticsRepository.findVoucherProfitsByBrandId(brandID);

        List<Map<String, Object>> profitData = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> data = new HashMap<>();
            data.put("profit", result[0]);
            data.put("eventName", result[1]);
            profitData.add(data);
        }
        return profitData;
    }
}

