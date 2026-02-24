package com.aiinpocket.webredgood.controller;

import com.aiinpocket.webredgood.dto.FollowerDistributionResponse;
import com.aiinpocket.webredgood.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "網紅管理", description = "網紅相關API")
@RestController
@RequestMapping("/api/influencers")
@RequiredArgsConstructor
public class InfluencerController {

    private final RecommendationService recommendationService;

    @Operation(summary = "查詢粉絲分布", description = "取得網紅粉絲的縣市分布")
    @GetMapping("/{id}/followers/distribution")
    public ResponseEntity<FollowerDistributionResponse> getFollowerDistribution
            (@Parameter(description = "網紅ID") @PathVariable Long id){
                    FollowerDistributionResponse body = recommendationService.getFollowerDistribution(id);
                if(body == null){
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(body);
    }
}
