package com.aiinpocket.webredgood.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "標籤", description = "標籤分析API")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
}
