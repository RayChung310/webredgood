package com.aiinpocket.webredgood.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowerDistributionResponse {

    @Schema(description = "網紅名稱", example = "阿滴")
    private String influencer;

    @Schema(description = "粉絲總人數", example = "30000")
    private Long totalFollowers;

    @Schema(description = "各縣市分布，依人數降序")
    private List<CityDistributionDto> distribution;

}
