package com.aiinpocket.webredgood.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 貼文模組錯誤碼，前綴: PST
 */
@Getter
@RequiredArgsConstructor
public enum PostError implements IErrorCode{

    // 001-099 查詢
    POST_NOT_FOUND("PST-001", "貼文不存在, postId = {}", HttpStatus.NOT_FOUND);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;


    @Override public String code() { return code; }
    @Override public String messageTemplate() { return messageTemplate; }
    @Override public HttpStatus httpStatus() { return httpStatus; }
}
