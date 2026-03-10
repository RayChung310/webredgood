package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.dto.UserTagResponse;
import com.aiinpocket.webredgood.entity.DimTag;
import com.aiinpocket.webredgood.entity.UserInterestTag;
import com.aiinpocket.webredgood.error.BizException;
import com.aiinpocket.webredgood.error.UserError;
import com.aiinpocket.webredgood.repository.TagRepository;
import com.aiinpocket.webredgood.repository.UserInterestTagRepository;
import com.aiinpocket.webredgood.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserTagService {

    private final UserRepository userRepository;
    private final UserInterestTagRepository userInterestTagRepository;

    // 得到用戶的興趣標籤
    @Transactional(readOnly = true)
    public List<UserTagResponse> getUserTags(Long userId){
        log.info("查詢用戶興趣標籤, userId={}", userId);
        if (userRepository.findById(userId).isEmpty()){
            throw new BizException(UserError.USER_NOT_FOUND, userId);
        }

        List<UserInterestTag> userInterestTagList = userInterestTagRepository.findByUserId(userId);
        List<UserTagResponse> userTagResponseList = userInterestTagList.stream()
                .map(userInterestTag -> new UserTagResponse(
                        userInterestTag.getTagId(),
                        userInterestTag.getTag() != null ? userInterestTag.getTag().getTagName() : null,
                        userInterestTag.getTag() != null ? userInterestTag.getTag().getCategory() : null,
                        userInterestTag.getWeight()
                ))
                .collect(Collectors.toList());

        log.info("用戶興趣標籤查詢完成, userId={}, 標籤筆數為={}", userId, userTagResponseList.size());
        return userTagResponseList;
    }

}
