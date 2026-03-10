package com.aiinpocket.webredgood.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequest {

    @NotNull(message = "postId 不可為空")
    @Schema(description = "被按讚的貼文ID", example = "1", required = true)
    private Long postId;
}
