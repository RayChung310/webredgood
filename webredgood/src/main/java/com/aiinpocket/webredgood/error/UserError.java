package com.aiinpocket.webredgood.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 用戶模組錯誤碼，前綴: USR
 */
@Getter
@RequiredArgsConstructor
public enum UserError implements IErrorCode{

    // 001-099 查詢
    USER_NOT_FOUND("USR-001", "用戶不存在, userId = {}", HttpStatus.NOT_FOUND);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;


    @Override public String code() { return code; }
    @Override public String messageTemplate() { return messageTemplate; }
    @Override public HttpStatus httpStatus() { return httpStatus; }
}
