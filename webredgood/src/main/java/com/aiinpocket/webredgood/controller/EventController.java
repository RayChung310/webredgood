package com.aiinpocket.webredgood.controller;

import com.aiinpocket.webredgood.dto.EventSuggestResponse;
import com.aiinpocket.webredgood.service.EventSuggestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "活動", description = "推薦活動地點與主題API")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventSuggestService eventSuggestService;

    @Operation(summary = "推薦活動地點與主題", description = "依照網紅與選填的標籤推薦舉辦地點與主題")
    @GetMapping("/suggest")
    public ResponseEntity<EventSuggestResponse> eventSuggestResponseResponseEntity(
            @Parameter(description = "網紅ID", required = true)
            @RequestParam ("influencerId") Long influencerId,
            @Parameter(description = "(選填)標籤ID")
            @RequestParam(value = "tag", required = false) Long tag){
        return ResponseEntity.ok(eventSuggestService.eventSuggestResponse(influencerId, tag));
    }


}
