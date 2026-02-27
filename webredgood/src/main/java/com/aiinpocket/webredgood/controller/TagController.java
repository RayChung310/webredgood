package com.aiinpocket.webredgood.controller;

import com.aiinpocket.webredgood.dto.CityRank;
import com.aiinpocket.webredgood.service.TagCityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "標籤", description = "標籤分析API")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagCityService tagCityService;

    @Operation(summary = "依標籤查詢城市排名", description = "按過該標前貼文的用戶依照城市排名")
    @GetMapping("/{tagId}/city-ranking")
    public ResponseEntity<List<CityRank>> getCityRank (
            @Parameter(description = "標籤ID")
            @PathVariable Long tagId){
        List<CityRank> body = tagCityService.getCityRankByTag(tagId);
        if (body == null) {
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(body);
    }


}
