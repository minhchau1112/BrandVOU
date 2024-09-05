package com.example.reportservice.model.statistics.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StatisticsResponse {
    private String eventName;
    private Double totalValueCount;

    public StatisticsResponse(String eventName, Double totalValueCount) {
        this.eventName = eventName;
        this.totalValueCount = totalValueCount;
    }
}
