package com.aiinpocket.webredgood.controller;

import com.aiinpocket.webredgood.dto.LikeRequest;
import com.aiinpocket.webredgood.service.TaggingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用戶", description = "用戶按讚與興趣標籤")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

     private final TaggingService taggingService;

     @Operation(summary = "紀錄用戶按讚貼文", description = "紀錄按讚並更新用戶興趣權重")
     @PostMapping("/{id}/likes")
     public ResponseEntity<Void> recordLike(
             @Parameter(description = "用戶ID")
             @PathVariable("id") Long id,
             @RequestBody LikeRequest likeRequest){
         // 傳入的RequestBody 或 PostId為空
         if (likeRequest == null || likeRequest.getPostId() == null) {
             return ResponseEntity.badRequest().build();
         }

         boolean ok = taggingService.recordLike(id, likeRequest.getPostId());
         return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
     }

}


