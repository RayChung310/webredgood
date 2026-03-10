package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.LikeResponse;
import com.aiinpocket.webredgood.entity.*;
import com.aiinpocket.webredgood.error.BizException;
import com.aiinpocket.webredgood.error.PostError;
import com.aiinpocket.webredgood.error.UserError;
import com.aiinpocket.webredgood.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaggingService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final FactUserLikeRepository factUserLikeRepository;
    private final UserInterestTagRepository userInterestTagRepository;
    private final DateRepository dateRepository;
    private final TagRepository tagRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public LikeResponse recordLike(Long userId, Long postId){

        boolean first = false;
        boolean tagsUpdated = false;

        // 有userId 跟 postId，視為已按讚，不增加興趣權重
        boolean alreadyLiked = factUserLikeRepository.findFirstByUser_IdAndPost_Id(userId, postId).isPresent();
        if (alreadyLiked){
            log.debug("用戶已對該貼文按讚，不再增加興趣權重, userId={}, postId={}", userId, postId);
            log.info("紀錄按讚結果結束(重複按讚，未變更興趣權重), userId={}, postId={}", userId, postId);
            return new LikeResponse(userId, postId, false, false, "用戶已按過讚");
        }

        // 不存在的情況
        Optional<DimUser> userOptional = userRepository.findById(userId);
        Optional<DimPost> postOptional = postRepository.findById(postId);

        if (userOptional.isEmpty()) {
            throw new BizException(UserError.USER_NOT_FOUND, userId);
        }
        if (postOptional.isEmpty()) {
            throw new BizException(PostError.POST_NOT_FOUND, postId);
        }

        // 存在的情況
        DimUser dimUser = userOptional.get();
        DimPost dimPost = postOptional.get();

        List<PostTag> postTags = postTagRepository.findByPostIdIn(List.of(postId));
        Long locationId = dimUser.getLocationId();
        // 依照當前日期動態查詢
        LocalDate today = LocalDate.now();
        Optional<DimDate> dateOptional = dateRepository.findByFullDate(today);
        Long dateId = dateOptional.map(DimDate::getId).orElse(1L);

        // 改為貼文有幾個標籤，就存幾筆 fact_user_like
        for (PostTag postTag : postTags){
            Long tagId = postTag.getTagId();
            FactUserLike factUserLike = new FactUserLike();
            factUserLike.setUser(userRepository.getReferenceById(userId));
            factUserLike.setPost(postRepository.getReferenceById(postId));
            factUserLike.setTag(tagRepository.getReferenceById(tagId));
            factUserLike.setLocation(locationId != null ? locationRepository.getReferenceById(locationId) : null);
            factUserLike.setDate(dateRepository.getReferenceById(dateId));
            factUserLike.setLikeCount(1);
            factUserLikeRepository.save(factUserLike);
        }

        // 第一次按讚，增加一次興趣權重
        updateUserInterestTagsForPost(userId, postId);
        log.info("按讚記錄成功, userId={}, postId={}", userId, postId);

        first = true;
        tagsUpdated = true;

        return new LikeResponse(userId, postId, first, tagsUpdated, "按讚成功");
    }

    private void updateUserInterestTagsForPost(Long userId, Long postId){

        // 取得貼文所有標籤
        List<PostTag> postTags = postTagRepository.findByPostIdIn(List.of(postId));
        // 時間時區處理
        Instant now = java.time.ZonedDateTime.now(java.time.ZoneId.of("Asia/Taipei")).toInstant();

        for(PostTag postTag : postTags){
            Long tagId = postTag.getTagId();
            // 查user是否有對該標籤貼文有按讚紀錄
            Optional<UserInterestTag> userInterestTagOptional = userInterestTagRepository.findByUserIdAndTagId(userId, tagId);
            if (userInterestTagOptional.isPresent()){
                UserInterestTag userInterestTag = userInterestTagOptional.get();
                Double currentWeight = Optional.ofNullable(userInterestTag.getWeight()).orElse(0.0);
                userInterestTag.setWeight(currentWeight + 1.0);
                userInterestTag.setLastUpdated(now);
                userInterestTagRepository.save(userInterestTag);
            }else {
                // 建立新的object
                UserInterestTag userInterestTag = new UserInterestTag();
                userInterestTag.setUserId(userId);
                userInterestTag.setTagId(tagId);
                userInterestTag.setWeight(1.0); // 初始權重 = 1.0
                userInterestTag.setLastUpdated(now);
                userInterestTagRepository.save(userInterestTag);

            }
        }
    }
}
