package com.aiinpocket.webredgood.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 通用錯誤碼 — 不歸屬於特定業務模組的共用錯誤。
 * 前綴: COM，段位: 001-099 請求/驗證，100-199 系統層級
 */
@Getter
@RequiredArgsConstructor
public enum CommonError implements IErrorCode {

    BAD_REQUEST       ("COM-001", "請求參數錯誤: {}",          HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED ("COM-002", "欄位驗證失敗: {}",          HttpStatus.BAD_REQUEST),
    UNAUTHORIZED      ("COM-003", "未授權，請先登入",           HttpStatus.UNAUTHORIZED),
    FORBIDDEN         ("COM-004", "權限不足，無法存取此資源",    HttpStatus.FORBIDDEN),
    NOT_FOUND         ("COM-005", "找不到指定資源: {}",         HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("COM-006", "不支援的 HTTP 方法: {}",    HttpStatus.METHOD_NOT_ALLOWED),

    INTERNAL_ERROR    ("COM-100", "系統內部錯誤，請稍後重試",    HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;

    @Override
    public String code() { return code; }

    @Override
    public String messageTemplate() { return messageTemplate; }

    @Override
    public HttpStatus httpStatus() { return httpStatus; }
}