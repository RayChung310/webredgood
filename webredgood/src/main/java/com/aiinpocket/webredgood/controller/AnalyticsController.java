package com.aiinpocket.webredgood.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "分析", description = "Star Schema聚合API")
@RestController
@RequestMapping("/api/analytics/star-schema")
@RequiredArgsConstructor
public class AnalyticsController {
}
