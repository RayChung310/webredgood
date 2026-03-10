package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.EventSuggestResponse;
import com.aiinpocket.webredgood.dto.RecommendCityResponse;
import com.aiinpocket.webredgood.entity.DimTag;
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

    public EventSuggestResponse eventSuggestResponse(Long influencerId, Long tagId){
        log.info("推薦活動與主題, influencerId={}, tagId={}", influencerId, tagId);
        RecommendCityResponse recommendCityResponse = recommendationService.recommendCity(influencerId, tagId);

        String tagName = null;
        if (tagId != null){
            tagName = tagRepository.findById(tagId).map(DimTag::getTagName).orElse(null);
        }

        EventSuggestResponse eventSuggestResponse = new EventSuggestResponse(
                recommendCityResponse.getRecommendedCity(),
                tagName,
                recommendCityResponse.getReason(),
                recommendCityResponse.getConfidenceScore()
        );
        log.info("推薦活動完成, 推薦城市={}, 推薦標籤={}",eventSuggestResponse.getRecommendCity(), eventSuggestResponse.getRecommendedTag());
        return eventSuggestResponse;
    }

}
