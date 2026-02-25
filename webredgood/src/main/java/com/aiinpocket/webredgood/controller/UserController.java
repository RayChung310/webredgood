package com.aiinpocket.webredgood.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用戶", description = "用戶按讚與興趣標籤")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {



}


