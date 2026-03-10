package com.aiinpocket.webredgood.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {

    private Long userId;
    private Long postId;

    // 是否為第一次按讚
    private boolean first;

    // 是否更新興趣標籤
    private boolean tagsUpdated;

    // 回應給前端的訊息
    private String message;
}
