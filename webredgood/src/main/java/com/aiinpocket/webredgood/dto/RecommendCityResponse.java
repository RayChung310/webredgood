package com.aiinpocket.webredgood.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendCityResponse {

    @Schema(description = "推薦縣市", example = "台中市")
    private String recommendedCity;

    @Schema(description = "推薦理由", example = "該縣市粉絲數量佔大宗")
    private String reason;

    @Schema(description = "信心分數", example = "0.1")
    private Double confidenceScore;

}
