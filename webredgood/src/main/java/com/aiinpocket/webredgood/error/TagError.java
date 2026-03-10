package com.aiinpocket.webredgood.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 標籤模組錯誤碼，前綴: TAG
 */
@Getter
@RequiredArgsConstructor
public enum TagError implements  IErrorCode{

    // 001-099 查詢
    TAG_NOT_FOUND("TAG-001", "標籤不存在, tagId = {}", HttpStatus.NOT_FOUND);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;


    @Override public String code() { return code; }
    @Override public String messageTemplate() { return messageTemplate; }
    @Override public HttpStatus httpStatus() { return httpStatus; }
}
