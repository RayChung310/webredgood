package com.aiinpocket.webredgood.service;

import com.aiinpocket.webredgood.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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





}
