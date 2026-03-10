package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.CitySummary;
import com.aiinpocket.webredgood.dto.TagSummary;
import com.aiinpocket.webredgood.repository.FactUserLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final FactUserLikeRepository factUserLikeRepository;

    @Transactional(readOnly = true)
    public List<CitySummary> citySummaryList() {
        log.info("Star Schema 聚合: 依照城市 (DB 端 GROUP BY)");
        List<CitySummary> result = factUserLikeRepository.getCitySummary();
        log.info("城市聚合查詢完成, 縣市數={}", result.size());
        return result;
    }

    @Transactional(readOnly = true)
    public List<TagSummary> tagSummaryList() {
        log.info("Star Schema 聚合: 依照標籤 (DB 端 GROUP BY)");

        List<TagSummary> result = factUserLikeRepository.getTagSummary();
        log.info("標籤聚合查詢完成, 標籤數={}", result.size());
        return result;

    }
}
