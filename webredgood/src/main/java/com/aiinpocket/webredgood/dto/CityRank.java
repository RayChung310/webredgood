package com.aiinpocket.webredgood.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityRank {

    @Schema(description = "縣市名稱")
    private String city;

    @Schema(description = "數量")
    private Long count;

    @Schema(description = "比例")
    private Double percentage;
}
