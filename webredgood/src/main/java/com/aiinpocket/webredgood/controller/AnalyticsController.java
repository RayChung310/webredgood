package com.aiinpocket.webredgood.controller;

import com.aiinpocket.webredgood.dto.CitySummary;
import com.aiinpocket.webredgood.dto.TagSummary;
import com.aiinpocket.webredgood.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "分析", description = "Star Schema聚合API")
@RestController
@RequestMapping("/api/analytics/star-schema")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(summary = "依城市聚合")
    @GetMapping("/city-summary")
    public List<CitySummary> citySummary(){
        return analyticsService.citySummaryList();
    }

    @Operation(summary = "依標籤聚合")
    @GetMapping("/tag-summary")
    public List<TagSummary> tagSummary(){
        return analyticsService.tagSummaryList();
    }

}
