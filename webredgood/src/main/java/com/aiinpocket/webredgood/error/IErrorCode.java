package com.aiinpocket.webredgood.error;

import org.springframework.http.HttpStatus;

/**
 * 統一錯誤碼契約 — 所有模組的錯誤碼列舉都必須實作此介面。
 *
 * <p>錯誤碼命名規範：PREFIX-NNN</p>
 * <ul>
 *   <li>COM  — 通用</li>
 *   <li>AUTH — 認證</li>
 *   <li>INF  — 網紅</li>
 *   <li>TAG  — 標籤</li>
 *   <li>USR  — 用戶</li>
 *   <li>ANL  — 分析</li>
 * </ul>
 * <p>每個前綴內建議分段：001-099 查詢、100-199 寫入/修改、200-299 狀態/流程</p>
 */
public interface IErrorCode {

    /** 錯誤碼，格式: PREFIX-NNN，例如 "COM-001" */
    String code();

    /** 訊息模板，支援 {} 佔位符（類似 SLF4J 格式） */
    String messageTemplate();

    /** 對應的 HTTP 狀態碼 */
    HttpStatus httpStatus();
}