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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final FactUserLikeRepository factUserLikeRepository;

    @Transactional(readOnly = true)
    public List<CitySummary> citySummaryList() {
        log.info("Star Schema 聚合: 依照城市 (Java Stream + GROUPING BY)");

        return factUserLikeRepository.findAll().stream()
                .filter(like -> like.getLocation() != null && like.getLocation().getCityName() != null)
                .collect(Collectors.groupingBy(like -> like.getLocation().getCityName(), Collectors.counting()))
                .entrySet().stream().map(entry -> new CitySummary(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TagSummary> tagSummaryList(){
        log.info("Star Schema 聚合: 依照標籤(Java Stream + GROUP BY)");

        return factUserLikeRepository.findAll().stream()
                .filter(like -> like.getTag() != null && like.getTag().getTagName() != null)
                .collect(Collectors.groupingBy(like -> like.getTag().getTagName(), Collectors.counting()))
                .entrySet().stream().map(entry -> new TagSummary(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingLong(TagSummary::getTotalCount).reversed())
                .toList();

    }
}
