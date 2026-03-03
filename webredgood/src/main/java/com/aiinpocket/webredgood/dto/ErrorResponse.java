package com.aiinpocket.webredgood.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 統一錯誤回應格式
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "統一錯誤回應")
public class ErrorResponse {

    @Schema(description = "錯誤碼", example = "COM-001")
    private String code;

    @Schema(description = "錯誤訊息", example = "找不到指定資源")
    private String message;

    @Schema(description = "錯誤細節（僅開發環境顯示）")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String detail;
}