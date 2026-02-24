package com.aiinpocket.webredgood.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityDistributionDto {

    @Schema(description = "縣市名稱", example = "台北市")
    private String city;

    @Schema(description = "粉絲人數", example = "9999")
    private Long count;

    @Schema(description = "百分比", example = "40.0")
    private Double percentage;
}
