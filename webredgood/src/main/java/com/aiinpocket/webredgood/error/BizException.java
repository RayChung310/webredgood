package com.aiinpocket.webredgood.error;

import lombok.Getter;

/**
 * 統一業務異常 — 攜帶結構化錯誤碼，由 GlobalExceptionHandler 統一攔截處理。
 *
 * <p>使用方式：</p>
 * <pre>
 *   throw new BizException(MyError.SOMETHING, arg1, arg2);
 *   throw new BizException(MyError.SOMETHING, "內部除錯訊息", arg1);
 * </pre>
 */
@Getter
public class BizException extends RuntimeException {

    private final IErrorCode errorCode;
    private final Object[] args;
    private final String internalDetail;

    public BizException(IErrorCode errorCode, Object... args) {
        this(errorCode, (String) null, (Throwable) null, args);
    }

    public BizException(IErrorCode errorCode, Throwable cause, Object... args) {
        this(errorCode, (String) null, cause, args);
    }

    public BizException(IErrorCode errorCode, String internalDetail, Object... args) {
        this(errorCode, internalDetail, (Throwable) null, args);
    }

    public BizException(IErrorCode errorCode, String internalDetail, Throwable cause, Object... args) {
        super(formatMessage(errorCode.messageTemplate(), args), cause);
        this.errorCode = errorCode;
        this.args = args;
        this.internalDetail = internalDetail;
    }

    /** 取得已替換佔位符的完整訊息 */
    public String getFormattedMessage() {
        return formatMessage(errorCode.messageTemplate(), args);
    }

    /** 將模板中的 {} 佔位符依序替換為 args */
    private static String formatMessage(String template, Object[] args) {
        if (args == null || args.length == 0) {
            return template;
        }
        StringBuilder sb = new StringBuilder();
        int argIndex = 0;
        int i = 0;
        while (i < template.length()) {
            if (i + 1 < template.length()
                    && template.charAt(i) == '{'
                    && template.charAt(i + 1) == '}'
                    && argIndex < args.length) {
                sb.append(args[argIndex++]);
                i += 2;
            } else {
                sb.append(template.charAt(i));
                i++;
            }
        }
        return sb.toString();
    }
}