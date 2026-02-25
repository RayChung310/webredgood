package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.entity.*;
import com.aiinpocket.webredgood.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Transactional
    public boolean recordLike(Long userId, Long postId){
        log.info("紀錄按讚, userId={}, postId={}", userId, postId);

        // 重複按讚的情況
        if (factUserLikeRepository.findFirstByUserIdAndPostId(userId, postId).isPresent()){
            log.debug("重複按讚的話，僅更新興趣的權重");
            updateUserInterestTagsForPost(userId, postId);
            return true;
        }

        // 不存在的情況
        Optional<DimUser> userOptional = userRepository.findById(userId);
        Optional<DimPost> postOptional = postRepository.findById(postId);

        if (userOptional.isEmpty() || postOptional.isEmpty()){
            log.warn("用戶或貼文不存在, userId={}, postId={}", userId, postId);
            return false;
        }

        // 存在的情況
        DimUser dimUser = userOptional.get();
        DimPost dimPost = postOptional.get();

        List<PostTag> postTags = postTagRepository.findByPostIdIn(List.of(postId));
        Long firstTagId = postTags.isEmpty() ? null : postTags.get(0).getTagId();
        Long locationId = dimUser.getLocationId();
        Long dateId = 1L;


        // 按讚紀錄儲存
        FactUserLike factUserLike = new FactUserLike();
        factUserLike.setUserId(userId);
        factUserLike.setPostId(postId);
        factUserLike.setTagId(firstTagId);
        factUserLike.setLocationId(locationId);
        factUserLike.setDateId(dateId);
        factUserLike.setLikeCount(1);
        factUserLikeRepository.save(factUserLike);

        updateUserInterestTagsForPost(userId, postId); // 增加興趣權重
        log.info("按讚記錄成功, userId={}, postId={}", userId, postId);
        return true;
    }

    private void updateUserInterestTagsForPost(Long userId, Long postId){
        List<PostTag> postTags = postTagRepository.findByPostIdIn(List.of(postId)); // 取得貼文所有標籤
        // 時間加八小時
        Instant now = Instant.now().plus(8, java.time.temporal.ChronoUnit.HOURS);

        for(PostTag postTag : postTags){
            Long tagId = postTag.getTagId();
            // 查user是否有對該標籤貼文有按讚紀錄
            Optional<UserInterestTag> userInterestTagOptional = userInterestTagRepository.findByUserIdAndTagId(userId, tagId);
            if (userInterestTagOptional.isPresent()){
                UserInterestTag userInterestTag = userInterestTagOptional.get();
                userInterestTag.setWeight(userInterestTag.getWeight() == null ? 0.0 : userInterestTag.getWeight() + 1.0 );
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
