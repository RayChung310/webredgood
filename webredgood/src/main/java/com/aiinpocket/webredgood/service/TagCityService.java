package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.CityRank;
import com.aiinpocket.webredgood.entity.DimUser;
import com.aiinpocket.webredgood.entity.FactUserLike;
import com.aiinpocket.webredgood.error.BizException;
import com.aiinpocket.webredgood.error.TagError;
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
    private final TagRepository tagRepository;

    // 判斷tagId
    public List<CityRank> getCityRankByTag(Long tagId){
        log.info("依標籤查詢城市排名, tagId為={}", tagId);
        if (tagRepository.findById(tagId).isEmpty()){
            throw new BizException(TagError.TAG_NOT_FOUND, tagId);
        }

        List<FactUserLike> like = factUserLikeRepository.findByTag_Id(tagId);
        if (like.isEmpty()){
            log.info("該標籤尚無按讚資料, tagId為={}", tagId);
            return List.of(); // 返回空列表
        }

        var cityCount = like.stream()
                .map(FactUserLike::getUser)
                .filter(u -> u != null && u.getCity() != null)
                .collect(Collectors.groupingBy(com.aiinpocket.webredgood.entity.DimUser::getCity, Collectors.counting()));

        long total = cityCount.values().stream().mapToLong(Long::longValue).sum();
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
