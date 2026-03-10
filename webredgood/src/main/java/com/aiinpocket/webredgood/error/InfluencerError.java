package com.aiinpocket.webredgood.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 網紅模組錯誤碼，前綴: INF
 */
@Getter
@RequiredArgsConstructor
public enum InfluencerError implements IErrorCode {

    // 001-099 查詢相關
    INFLUENCER_NOT_FOUND("INF-001", "網紅不存在, influencerId={}", HttpStatus.NOT_FOUND),

    // 100-199 粉絲分布
    NO_FOLLOWER_DISTRIBUTION("INF-002", "網紅沒有粉絲分布資料, influencerId={}", HttpStatus.NOT_FOUND);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;

    @Override public String code() { return code; }
    @Override public String messageTemplate() { return messageTemplate; }
    @Override public HttpStatus httpStatus() { return httpStatus; }
}