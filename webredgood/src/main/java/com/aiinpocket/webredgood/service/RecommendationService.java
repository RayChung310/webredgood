package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.CityDistributionDto;
import com.aiinpocket.webredgood.dto.FollowerDistributionResponse;
import com.aiinpocket.webredgood.entity.DimInfluencer;
import com.aiinpocket.webredgood.entity.DimPost;
import com.aiinpocket.webredgood.entity.DimUser;
import com.aiinpocket.webredgood.entity.FactUserLike;
import com.aiinpocket.webredgood.repository.FactUserLikeRepository;
import com.aiinpocket.webredgood.repository.InfluencerRepository;
import com.aiinpocket.webredgood.repository.PostRepository;
import com.aiinpocket.webredgood.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final InfluencerRepository influencerRepository; // 網紅 ID 查網紅
    private final PostRepository postRepository; //網紅 ID 查網紅貼文
    private final FactUserLikeRepository factUserLikeRepository; //貼文 ID 查按讚紀錄
    private final UserRepository userRepository; //user_id 列表查用戶縣市

    public FollowerDistributionResponse getFollowerDistribution(Long influencerId){
        log.info("開始查找網紅的粉絲分布, influencerId={} ", influencerId);

        Optional<DimInfluencer> influencerOpt = influencerRepository.findById(influencerId);

        if (influencerOpt.isEmpty()){
            log.warn("網紅不存在, influencerId: " + influencerId);
            return null;
        }

        DimInfluencer influencer= influencerOpt.get();

        List<DimPost> posts = postRepository.findByInfluencerId(influencerId);
        if(posts.isEmpty()){
            log.info("該網紅尚無貼文, influencerId={} " + influencerId);
            return new FollowerDistributionResponse(influencer.getName(), 0L, List.of());
        }

        List<Long> postIds = posts.stream().map(DimPost::getId).toList();
        List<FactUserLike> likes = factUserLikeRepository.findByPostIdIn(postIds);

        List<Long> disrinctUserIds = likes.stream().map(FactUserLike::getUserId)
                .distinct().toList();
        if (disrinctUserIds.isEmpty()){
            log.info("網紅該貼文尚無按讚, influencerId={}"+ influencerId);
            return new FollowerDistributionResponse(influencer.getName(), 0L, List.of());
        }

        List<DimUser> users = userRepository.findAllById(disrinctUserIds);
        long total = users.size();

        var cityToCount = users.stream().collect(Collectors.groupingBy(DimUser::getCity, Collectors.counting()));

        List<CityDistributionDto> distribution = cityToCount.entrySet().stream()
                .map(e -> new CityDistributionDto(
                        e.getKey(),
                        e.getValue(),
                        total > 0 ? Math.round(e.getValue() * 10000.0 / total) / 100.0 : 0.0
                ))
                .sorted((a,b) -> Long.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());

        log.info("粉絲分布查詢完成, influencerId={} , 總人數={} , 縣市數={} ", influencerId, total, distribution.size());
        return new FollowerDistributionResponse(influencer.getName(), total, distribution);
    }
}
