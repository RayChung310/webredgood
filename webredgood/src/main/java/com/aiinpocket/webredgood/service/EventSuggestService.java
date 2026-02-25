package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.EventSuggestResponse;
import com.aiinpocket.webredgood.dto.RecommendCityResponse;
import com.aiinpocket.webredgood.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventSuggestService {

    private final RecommendationService recommendationService;
    private final TagRepository tagRepository;


}
