package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.CitySummary;
import com.aiinpocket.webredgood.dto.TagSummary;
import com.aiinpocket.webredgood.entity.FactUserLike;
import com.aiinpocket.webredgood.repository.FactUserLikeRepository;
import com.aiinpocket.webredgood.repository.LocationRepository;
import com.aiinpocket.webredgood.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final FactUserLikeRepository factUserLikeRepository;

    public List<CitySummary> citySummaryList() {
        log.info("Star Schema 聚合: 依照城市 (JPQL JOIN f.location + GROUPING BY)");
        return factUserLikeRepository.getCitySummary();
    }

    public List<TagSummary> tagSummaryList(){
        log.info("Star Schema 聚合: 依照標籤(JPQL JOIN f.tag + GROUP BY)");
        return factUserLikeRepository.getTagSummary();
    }

}
