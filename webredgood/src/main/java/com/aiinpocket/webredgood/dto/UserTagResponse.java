package com.aiinpocket.webredgood.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTagResponse {

    @Schema(description = "標籤ID")
    private Long tagId;

    @Schema(description = "標籤名稱")
    private String tagName;

    @Schema(description = "分類")
    private String category;

    @Schema(description = "權重")
    private Double weight;
}
