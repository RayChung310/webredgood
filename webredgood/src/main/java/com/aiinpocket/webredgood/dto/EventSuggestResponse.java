package com.aiinpocket.webredgood.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSuggestResponse {

    @Schema(description = "推薦城市")
    private String recommendCity;

    @Schema(description = "推薦標籤")
    private String recommendedTag;

    @Schema(description = "原因說明")
    private String reason;

    @Schema(description = "信心分數")
    private Double cinfidenceScore;
}
