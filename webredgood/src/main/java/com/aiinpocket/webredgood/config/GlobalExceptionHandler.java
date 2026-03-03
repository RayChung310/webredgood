package com.aiinpocket.webredgood.config;

import com.aiinpocket.webredgood.dto.ErrorResponse;
import com.aiinpocket.webredgood.error.BizException;
import com.aiinpocket.webredgood.error.CommonError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

/**
 * 全域例外處理 — 統一攔截各類異常，回傳結構化的 ErrorResponse。
 * 內部記錄完整堆疊，對外只回傳安全的錯誤碼與訊息。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${app.error.expose-detail:false}")
    private boolean exposeDetail;

    /** 業務異常 */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleBizException(BizException ex) {
        log.warn("業務異常: code={}, message={}, internalDetail={}",
                ex.getErrorCode().code(),
                ex.getFormattedMessage(),
                ex.getInternalDetail(),
                ex);

        ErrorResponse body = new ErrorResponse(
                ex.getErrorCode().code(),
                ex.getFormattedMessage(),
                exposeDetail ? ex.getInternalDetail() : null
        );
        return ResponseEntity.status(ex.getErrorCode().httpStatus()).body(body);
    }

    /** Bean Validation 驗證失敗 (@Valid) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("欄位驗證失敗: {}", fieldErrors, ex);

        ErrorResponse body = new ErrorResponse(
                CommonError.VALIDATION_FAILED.code(),
                formatTemplate(CommonError.VALIDATION_FAILED.messageTemplate(), fieldErrors),
                exposeDetail ? ex.getMessage() : null
        );
        return ResponseEntity.badRequest().body(body);
    }

    /** 缺少必要的請求參數 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        log.warn("缺少請求參數: {}", ex.getParameterName(), ex);

        ErrorResponse body = new ErrorResponse(
                CommonError.BAD_REQUEST.code(),
                formatTemplate(CommonError.BAD_REQUEST.messageTemplate(), ex.getParameterName()),
                exposeDetail ? ex.getMessage() : null
        );
        return ResponseEntity.badRequest().body(body);
    }

    /** 請求參數類型轉換失敗 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String detail = String.format("參數 '%s' 的值 '%s' 無法轉換為 %s",
                ex.getName(), ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "未知類型");

        log.warn("參數類型轉換失敗: {}", detail, ex);

        ErrorResponse body = new ErrorResponse(
                CommonError.BAD_REQUEST.code(),
                formatTemplate(CommonError.BAD_REQUEST.messageTemplate(), detail),
                exposeDetail ? ex.getMessage() : null
        );
        return ResponseEntity.badRequest().body(body);
    }

    /** 請求 Body 無法解析 (JSON 格式錯誤等) */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
        log.warn("請求 Body 無法解析", ex);

        ErrorResponse body = new ErrorResponse(
                CommonError.BAD_REQUEST.code(),
                formatTemplate(CommonError.BAD_REQUEST.messageTemplate(), "請求 Body 格式錯誤"),
                exposeDetail ? ex.getMessage() : null
        );
        return ResponseEntity.badRequest().body(body);
    }

    /** HTTP 方法不支援 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("不支援的 HTTP 方法: {}", ex.getMethod(), ex);

        ErrorResponse body = new ErrorResponse(
                CommonError.METHOD_NOT_ALLOWED.code(),
                formatTemplate(CommonError.METHOD_NOT_ALLOWED.messageTemplate(), ex.getMethod()),
                exposeDetail ? ex.getMessage() : null
        );
        return ResponseEntity.status(CommonError.METHOD_NOT_ALLOWED.httpStatus()).body(body);
    }

    /** Spring 路徑找不到 */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResource(NoResourceFoundException ex) {
        log.warn("資源路徑不存在: {}", ex.getResourcePath(), ex);

        ErrorResponse body = new ErrorResponse(
                CommonError.NOT_FOUND.code(),
                formatTemplate(CommonError.NOT_FOUND.messageTemplate(), ex.getResourcePath()),
                exposeDetail ? ex.getMessage() : null
        );
        return ResponseEntity.status(CommonError.NOT_FOUND.httpStatus()).body(body);
    }

    /** 兜底: 所有未預期的異常 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("未預期的系統異常", ex);

        ErrorResponse body = new ErrorResponse(
                CommonError.INTERNAL_ERROR.code(),
                CommonError.INTERNAL_ERROR.messageTemplate(),
                exposeDetail ? ex.getMessage() : null
        );
        return ResponseEntity.status(CommonError.INTERNAL_ERROR.httpStatus()).body(body);
    }

    /** 簡易模板替換，將第一個 {} 替換為 arg */
    private String formatTemplate(String template, String arg) {
        return template.replaceFirst("\\{}", arg != null ? arg : "");
    }
}