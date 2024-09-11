package com.example.eventservice.controller;

import com.example.eventservice.service.BudgetService;
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
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/budget-statistics")
    public ResponseEntity<List<Map<String, Object>>> getStatistics(@RequestParam("brandID") Long brandID) {
        List<Map<String, Object>> statistics = budgetService.getBudgetByBrandId(brandID);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/participant-statistics")
    public ResponseEntity<List<Map<String, Object>>> getParticipants(@RequestParam("brandID") Long brandID) {
        List<Map<String, Object>> participants = budgetService.getParticipantCountByBrandId(brandID);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }
}
