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
public class LikeResponse {

    @Schema(description = "用戶 ID")
    private Long userId;

    @Schema(description = "貼文 ID")
    private Long postId;

    @Schema(description = "是否為第一次按讚")
    private boolean first;

    @Schema(description = "是否更新興趣標籤")
    private boolean tagsUpdated;

    @Schema(description = "回應給前端的訊息")
    private String message;
}
