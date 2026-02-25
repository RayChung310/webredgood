package com.aiinpocket.webredgood.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequest {

    @Schema(description = "被按讚的貼文ID", example = "1", required = true)
    private Long postId;
}
