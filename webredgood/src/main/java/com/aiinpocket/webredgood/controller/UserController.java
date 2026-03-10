package com.aiinpocket.webredgood.controller;

import com.aiinpocket.webredgood.dto.LikeRequest;
import com.aiinpocket.webredgood.dto.UserTagResponse;
import com.aiinpocket.webredgood.service.TaggingService;
import com.aiinpocket.webredgood.service.UserTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用戶", description = "用戶按讚與興趣標籤")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

     private final TaggingService taggingService;
     private final UserTagService userTagService;

     @Operation(summary = "紀錄用戶按讚貼文", description = "紀錄按讚並更新用戶興趣權重")
     @PostMapping("/{id}/likes")
     public ResponseEntity<Void> recordLike(
             @Parameter(description = "用戶ID")
             @PathVariable("id") Long id,
             @RequestBody @Valid LikeRequest likeRequest){

         taggingService.recordLike(id, likeRequest.getPostId());
         return ResponseEntity.ok().build();
     }

     @Operation(summary = "查詢用戶興趣標籤", description = "取得該用戶的興趣標籤與權重")
     @GetMapping("/{id}/tags")
     public ResponseEntity<List<UserTagResponse>> getUserTags(
             @Parameter(description = "用戶ID")
             @PathVariable("id") Long id){

         return ResponseEntity.ok( userTagService.getUserTags(id));
     }

}


