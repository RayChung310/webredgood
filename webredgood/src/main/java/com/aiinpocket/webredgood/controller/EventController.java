package com.aiinpocket.webredgood.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "活動", description = "推薦活動地點與主題API")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {



}
