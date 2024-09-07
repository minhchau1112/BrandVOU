package com.example.reportservice.controller;

import com.example.reportservice.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/reports")
    public ResponseEntity<List<Map<String, Object>>> getStatistics(@RequestParam("brandID") Long brandID) {
        List<Map<String, Object>> statistics = statisticsService.getBudgetByBrandId(brandID);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/participants")
    public ResponseEntity<List<Map<String, Object>>> getParticipants(@RequestParam("brandID") Long brandID) {
        List<Map<String, Object>> participants = statisticsService.getParticipantCountByBrandId(brandID);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }
}

