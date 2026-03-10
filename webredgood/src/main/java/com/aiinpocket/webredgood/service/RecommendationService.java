package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.CityDistributionDto;
import com.aiinpocket.webredgood.dto.FollowerDistributionResponse;
import com.aiinpocket.webredgood.dto.RecommendCityResponse;
import com.aiinpocket.webredgood.entity.*;
import com.aiinpocket.webredgood.repository.*;
import com.aiinpocket.webredgood.error.BizException;
import com.aiinpocket.webredgood.error.InfluencerError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PostTagRepository postTagRepository; //標籤跟貼文的篩選

    // 沒有tag
    @Transactional(readOnly = true)
    public FollowerDistributionResponse getFollowerDistribution(Long influencerId) {
        log.info("查詢粉絲分布(無標籤), influencerId={}", influencerId);
        FollowerDistributionResponse followerDistributionResponse = getFollowerDistributionInternal(influencerId, null);
        log.info("查詢粉絲分布結束(無標籤), influencerId={}", influencerId);
        return followerDistributionResponse;
    }

    // 有tag
    @Transactional(readOnly = true)
    public FollowerDistributionResponse getFollowerDistributionByTag(Long influencerId, Long tagId){
        log.info("查詢粉絲分布(依照標籤), influencerId={}, tagId={}", influencerId, tagId);
        FollowerDistributionResponse followerDistributionResponse = getFollowerDistributionInternal(influencerId, tagId);
        log.info("查詢粉絲分布結束(依照標籤), influencerId={}, tagId={}", influencerId, tagId);
        return followerDistributionResponse;
    }

    public FollowerDistributionResponse getFollowerDistributionInternal(Long influencerId, Long tagId)
    {
        log.info("開始查找網紅的粉絲分布, influencerId={}, tagId={}", influencerId, tagId);

        Optional<DimInfluencer> influencerOpt = influencerRepository.findById(influencerId);

        if (influencerOpt.isEmpty()){
            throw new BizException(InfluencerError.INFLUENCER_NOT_FOUND, influencerId);
        }

        DimInfluencer influencer= influencerOpt.get();

        List<DimPost> posts = postRepository.findByInfluencerId(influencerId);
        if(posts.isEmpty()){
            log.info("該網紅尚無貼文, influencerId={} ", influencerId);
            return new FollowerDistributionResponse(influencer.getName(), 0L, List.of());
        }

        List<Long> postIds = posts.stream().map(DimPost::getId).toList();
        List<FactUserLike> likes = factUserLikeRepository.findByPost_IdIn(postIds);

        // 找有該tag的postId，再保留貼文按讚
        if (tagId != null){
            List<Long> postIdsWithTag = postTagRepository.findByPostIdInAndTagId(postIds, tagId)
                    .stream().map(PostTag::getPostId)
                    .toList();

            likes = likes.stream()
                    .filter(like -> postIdsWithTag.contains(like.getPostId()))
                    .toList();
        }


        List<Long> disrinctUserIds = likes.stream()
                .map(FactUserLike::getUserId)
                .distinct()
                .toList();
        if (disrinctUserIds.isEmpty()){
            log.info("網紅該貼文尚無或沒有符合tag的按讚，, influencerId={}", influencerId);
            return new FollowerDistributionResponse(influencer.getName(), 0L, List.of());
        }

        List<DimUser> users = userRepository.findAllById(disrinctUserIds);
        long total = users.size();

        var cityToCount = users.stream()
                .collect(Collectors
                        .groupingBy(DimUser::getCity, Collectors.counting()));

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

    // 推薦活動舉辦城市，取粉絲分布第一名，用tag篩選
    @Transactional(readOnly = true)
    public RecommendCityResponse recommendCity(Long influencerId, Long tagId){
        log.info("開始推薦活動地點, influencerId={}, tagId={} ", influencerId, tagId);

        FollowerDistributionResponse followerDistributionResponse = tagId != null
                ? getFollowerDistributionByTag(influencerId, tagId)
                : getFollowerDistribution(influencerId);

        // 網紅不存在時 getFollowerDistribution 會直接拋出 BizException，不需要 null check
        // 分布可能是null或是空[]
        if (followerDistributionResponse.getDistribution() == null || followerDistributionResponse.getDistribution().isEmpty()){
            log.info("無法推薦，因為沒有粉絲分布資料, influencerId={}", influencerId);
            return new RecommendCityResponse(
                    null,
                    "因為沒有粉絲分布資料，無法推薦活動地點",
                    0.0
            );
        }

        CityDistributionDto cityDistributionDto = followerDistributionResponse.getDistribution().get(0);
        double confidence = cityDistributionDto.getPercentage() != null ? cityDistributionDto.getPercentage() / 100.0 : 0.0;
        String reason = String.format("該城市粉絲數量最多，占%.1f%% ", cityDistributionDto.getPercentage());


        log.info("推薦成功，influencerId={}, 推薦城市={}, 信心分數={}", influencerId, cityDistributionDto.getCity(), confidence);
        return new RecommendCityResponse(
                cityDistributionDto.getCity(),
                reason,
                confidence
        );
    }

}
