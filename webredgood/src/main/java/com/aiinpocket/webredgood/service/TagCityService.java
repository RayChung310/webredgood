package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.CityRank;
import com.aiinpocket.webredgood.entity.DimUser;
import com.aiinpocket.webredgood.entity.FactUserLike;
import com.aiinpocket.webredgood.repository.FactUserLikeRepository;
import com.aiinpocket.webredgood.repository.TagRepository;
import com.aiinpocket.webredgood.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagCityService {

    private final FactUserLikeRepository factUserLikeRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    // 判斷tagId
    public List<CityRank> getCityRankByTag(Long tagId){
        log.info("依標籤查詢城市排名, tagId為={}", tagId);
        if (tagRepository.findById(tagId).isEmpty()){
            log.warn("標籤不存在, tagId為={}", tagId);
            return null;
        }

        List<FactUserLike> like = factUserLikeRepository.findByTagId(tagId);
        List<Long> userId = like.stream()
                .map(FactUserLike::getUserId)
                .distinct() // 去重，看總人數
                .toList();
        if (userId.isEmpty()){
            log.info("該標籤尚無按讚資料, tagId為={}", tagId);
            return List.of(); // 返回空列表
        }

        List<DimUser> user = userRepository.findAllById(userId);
        long total = user.size();

        var cityCount = user.stream().collect(Collectors.groupingBy(DimUser::getCity, Collectors.counting()));
        List<CityRank> result = cityCount.entrySet().stream()
                .map(e -> new CityRank(
                        e.getKey(),
                        e.getValue(),
                        total > 0 ? Math.round(e.getValue() * 10000.0 / total) / 100.0 : 0.0
                ))
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());
        log.info("城市排名查詢完成, tagId={}, 縣市數={}", tagId, result.size());
        return result;
    }
}
