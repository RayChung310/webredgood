package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.EventSuggestResponse;
import com.aiinpocket.webredgood.dto.RecommendCityResponse;
import com.aiinpocket.webredgood.entity.DimTag;
import com.aiinpocket.webredgood.error.BizException;
import com.aiinpocket.webredgood.error.TagError;
import com.aiinpocket.webredgood.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventSuggestService {

    private final RecommendationService recommendationService;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public EventSuggestResponse eventSuggestResponse(Long influencerId, Long tagId){
        log.info("推薦活動與主題, influencerId={}, tagId={}", influencerId, tagId);

        // 若tagId 不存在，改成404錯誤
        Optional<DimTag> dimTagOptional = Optional.empty();
        if (tagId != null) {
            dimTagOptional = tagRepository.findById(tagId);
            if (dimTagOptional.isEmpty()) {
                throw new BizException(TagError.TAG_NOT_FOUND, tagId);
            }
        }

        RecommendCityResponse recommendCityResponse = recommendationService.recommendCity(influencerId, tagId);

        // 若tagId 存在，取出tagName
        String tagName = null;
        if (tagId != null){
            tagName = dimTagOptional.get().getTagName();
        }

        EventSuggestResponse eventSuggestResponse = new EventSuggestResponse(
                recommendCityResponse.getRecommendedCity(),
                tagName,
                recommendCityResponse.getReason(),
                recommendCityResponse.getConfidenceScore()
        );
        log.info("推薦活動完成, 推薦城市={}, 推薦標籤={}",eventSuggestResponse.getRecommendedCity(), eventSuggestResponse.getRecommendedTag());
        return eventSuggestResponse;
    }

}
