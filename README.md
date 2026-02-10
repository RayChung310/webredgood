# practise-1：網紅活動地點推薦系統

> Java 後端工程師培訓計畫 — 練習一

## 專案簡介

當一位網紅想要舉辦線下活動時，最重要的問題是：「活動該辦在哪裡？」以及「什麼主題最受粉絲歡迎？」

本專案要求建構一個 Spring Boot 後端系統，透過分析粉絲的居住地與興趣偏好，為網紅提供最佳的活動舉辦地點與主題建議。

**系統核心理念：**
- 分析粉絲居住地（縣市）分佈，找出粉絲最密集的地區作為推薦地點
- 透過用戶「按讚」的貼文來自動標記用戶的興趣標籤，了解粉絲對哪些主題有興趣
- 使用星狀圖（Star Schema）作為分析型資料庫架構，支援多維度的聚合查詢

## 技術棧

| 技術 | 說明 |
|------|------|
| **Java 21** | 專案使用的程式語言版本（LTS 長期支援版） |
| **Spring Boot** | 主要開發框架，簡化設定與快速建立應用程式 |
| **Spring Data JPA** | 資料庫存取框架，搭配 Hibernate 操作資料庫 |
| **PostgreSQL** | 關聯式資料庫，儲存所有系統資料 |
| **Lombok** | 自動產生 Getter / Setter 等重複程式碼的工具 |
| **Springdoc OpenAPI (Swagger)** | API 文件自動產生工具，必須整合 |
| **Maven** | 專案建置與套件管理工具 |
| **Postman** | API 測試工具，用於驗證所有端點的正確性 |

## 技術說明

### Java 21

本專案使用 Java 21 LTS 版本。初學階段請先專注於以下基礎：

- 熟悉基本語法：類別、介面、繼承、多型
- 了解集合框架：`List`、`Map`、`Set` 的使用
- 熟悉 Lambda 表達式與 Stream API（用於資料處理與過濾）
- 了解 Optional 的用法（JPA 查詢常會用到）

### Spring Data JPA

資料存取層使用 Spring Data JPA，需掌握以下基礎：

- 使用 `@Entity` 定義資料表對應的 Java 類別
- 建立 Repository 介面繼承 `JpaRepository`，自動獲得基本 CRUD 方法
- 學會用方法命名規則來自動產生查詢（例如 `findByCity(String city)`）
- 複雜查詢使用 `@Query` 註解搭配 JPQL 或原生 SQL
- 正確設定 Entity 之間的關聯（`@OneToMany`、`@ManyToOne`、`@ManyToMany`）

### PostgreSQL

資料庫相關重點：

- 本地開發可使用 Docker 啟動，或直接安裝 PostgreSQL
- 資料表與欄位命名使用 **snake_case**（例如：`dim_user`、`created_at`）
- 在 `application.properties` 中設定連線資訊
- 準備 `data.sql` 作為初始測試資料

### Lombok

減少重複程式碼的工具，常用註解：

| 註解 | 功能 |
|------|------|
| `@Getter` / `@Setter` | 自動產生 Getter 和 Setter 方法 |
| `@NoArgsConstructor` | 自動產生無參數建構子 |
| `@AllArgsConstructor` | 自動產生全參數建構子 |
| `@Builder` | 提供 Builder 模式建立物件 |
| `@Slf4j` | 自動注入 Logger，方便印出日誌 |
| `@Data` | 同時產生 Getter/Setter/toString/equals/hashCode |

> **提醒**：Entity 類別建議使用 `@Getter` + `@Setter`，不要用 `@Data`，避免 JPA 的 equals/hashCode 問題。

---

## ⭐ 重要開發技能：Log 與 Debug

> 寫程式不只是把功能做出來，**能快速定位問題、追蹤程式行為**才是真正的工程師核心能力。
> 本專案要求你在開發過程中**養成良好的 Log 與 Debug 習慣**，這比寫出功能本身更重要。

### 一、Log（日誌記錄）

#### 為什麼重要？

程式部署到伺服器後，你沒辦法像本機一樣直接 Debug。唯一能幫助你了解「系統發生了什麼事」的就是 Log。好的 Log 習慣能讓你在出問題時 **5 分鐘內找到原因**，而不是花 3 小時亂猜。

#### 使用方式

使用 Lombok 的 `@Slf4j` 註解，每個 Class 都能直接使用 `log` 物件：

```java
@Slf4j
@Service
public class RecommendationService {

    public List<CityDistribution> getFollowerDistribution(Long influencerId) {
        log.info("開始查詢網紅粉絲分佈, influencerId={}", influencerId);

        List<User> followers = userRepository.findByInfluencerId(influencerId);
        log.debug("查詢到 {} 位粉絲", followers.size());

        if (followers.isEmpty()) {
            log.warn("網紅 {} 沒有任何粉絲資料", influencerId);
            return Collections.emptyList();
        }

        Map<String, Long> cityCount = followers.stream()
                .collect(Collectors.groupingBy(User::getCity, Collectors.counting()));
        log.debug("縣市分佈統計結果: {}", cityCount);

        List<CityDistribution> result = buildDistribution(cityCount, followers.size());
        log.info("粉絲分佈查詢完成, influencerId={}, 共 {} 個縣市", influencerId, result.size());

        return result;
    }
}
```

#### Log 等級說明

| 等級 | 用途 | 範例 |
|------|------|------|
| `log.error()` | 系統錯誤、例外發生，需要立即處理 | 資料庫連線失敗、NullPointerException |
| `log.warn()` | 不正常但系統仍可運作的狀況 | 查詢結果為空、參數值異常但有預設值 |
| `log.info()` | 重要的業務流程紀錄 | API 被呼叫、核心功能開始/結束 |
| `log.debug()` | 開發除錯用的詳細資訊 | 變數值、SQL 查詢結果、中間計算過程 |
| `log.trace()` | 非常細粒度的追蹤（很少用） | 迴圈中每一步的狀態 |

#### Log 的最佳實踐

**✅ 應該記錄的：**
```java
// 1. 每個 API 進入點
log.info("收到請求: GET /api/influencers/{}/recommend-city", influencerId);

// 2. 重要的業務決策
log.info("推薦結果: 網紅={}, 推薦縣市={}, 信心分數={}", name, city, confidence);

// 3. 例外處理
log.error("查詢粉絲分佈失敗, influencerId={}", influencerId, exception);

// 4. 外部服務呼叫
log.info("開始查詢資料庫, SQL={}", query);
log.info("資料庫查詢完成, 耗時={}ms, 結果筆數={}", duration, count);
```

**❌ 不應該記錄的：**
```java
// 1. 敏感資訊
log.info("用戶登入, password={}", password);  // ← 絕對不行！

// 2. 過於頻繁的迴圈日誌（會導致 log 檔案爆炸）
for (User user : users) {
    log.info("處理用戶: {}", user);  // ← 幾萬筆就會塞爆
}

// 3. 無意義的日誌
log.info("進入方法");  // ← 沒有上下文，毫無用處
```

#### application.properties 設定

```properties
# 設定不同套件的 Log 等級
logging.level.root=INFO
logging.level.com.aiinpocket.webredgood=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# 輸出到檔案（方便事後查看）
logging.file.name=logs/webredgood.log
logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
```

> **作業要求**：每個 Service 方法至少要有**進入 (info)** 和**結束 (info)** 兩筆 Log，異常情況要有 **error** 或 **warn** 等級的 Log。

---

### 二、Debug（除錯技巧）

#### 為什麼重要？

初學者遇到 Bug 常常只會用 `System.out.println()` 到處印值，這不僅效率低，而且程式碼上線前還要一一刪除。學會使用 IDE 的 Debug 工具，能讓你 **逐行追蹤程式執行**，即時查看所有變數的值，是找問題最快的方式。

#### IntelliJ IDEA Debug 基本操作

| 操作 | 快捷鍵（Mac / Windows） | 說明 |
|------|--------------------------|------|
| 設定中斷點 | 點擊程式碼行號左側 | 程式執行到這裡會暫停 |
| Debug 模式啟動 | `Ctrl+D` / `Shift+F9` | 以除錯模式啟動 Spring Boot |
| 逐行執行（Step Over） | `F8` | 執行當前行，跳到下一行 |
| 進入方法（Step Into） | `F7` | 進入當前行呼叫的方法內部 |
| 跳出方法（Step Out） | `Shift+F8` | 執行完當前方法，回到呼叫處 |
| 繼續執行（Resume） | `F9` | 繼續執行直到下一個中斷點 |
| 查看變數值 | Variables 面板 | 查看當前作用域的所有變數 |
| 即時計算表達式 | `Alt+F8` / Evaluate | 執行任意表達式查看結果 |
| 條件中斷點 | 右鍵中斷點 → Condition | 只在特定條件成立時才暫停 |

#### Debug 實戰情境

**情境 1：API 回傳結果不對**
```
1. 在 Controller 方法第一行設定中斷點
2. 用 Postman 送出請求
3. 程式會在中斷點暫停
4. 逐行執行 (F8)，觀察每個變數的值
5. 找到值不對的那一行 → 就是 Bug 所在
```

**情境 2：NullPointerException**
```
1. 看 Console 的錯誤訊息，找到出錯的行號
2. 在那一行設定中斷點
3. 重新發送請求觸發 Bug
4. 檢查哪個變數是 null
5. 往上追蹤：這個變數是從哪裡來的？為什麼是 null？
```

**情境 3：SQL 查詢結果不如預期**
```
1. 在 Repository 呼叫處設定中斷點
2. 觀察傳入的參數是否正確
3. 開啟 Hibernate SQL Log（見上方 Log 設定）查看實際執行的 SQL
4. 把 SQL 貼到資料庫工具（DBeaver / pgAdmin）直接執行
5. 比對結果差異
```

#### Evaluate Expression 技巧

Debug 暫停時，可以用 Evaluate Expression（`Alt+F8`）即時測試：

```java
// 假設正在 Debug，可以即時輸入以下表達式查看結果：
followers.size()
followers.stream().filter(f -> f.getCity().equals("台北市")).count()
cityCount.get("台北市")
result.get(0).getPercentage()
```

這比 `System.out.println()` 強大太多 — 你可以即時測試任何表達式，而不需要修改程式碼。

#### Debug 的最佳實踐

**✅ 好習慣：**
- 遇到 Bug 第一反應是「設中斷點」而不是「加 println」
- 善用條件中斷點，避免迴圈中每次都暫停
- Debug 時注意觀察 Variables 面板中集合的大小和內容
- 搭配 Log 一起使用：先看 Log 縮小範圍，再用 Debug 精準定位

**❌ 壞習慣：**
- 到處寫 `System.out.println()` 來除錯
- 遇到問題就改 code 碰運氣，不先理解原因
- 只看錯誤訊息的第一行，忽略完整的 Stack Trace
- 不看 Log 就直接問別人「為什麼不能跑」

---

## 資料庫設計（Star Schema）

本系統採用星狀圖架構，中心的 Fact Table 記錄按讚事件，周圍的 Dimension Table 提供各維度的描述。

### Dimension Tables（維度表）

| 資料表 | 說明 | 主要欄位 |
|--------|------|----------|
| `dim_user` | 用戶資訊 | user_id, username, email, city, county |
| `dim_post` | 貼文資訊 | post_id, influencer_id, title, content |
| `dim_tag` | 興趣標籤 | tag_id, tag_name, category |
| `dim_location` | 地點資訊 | location_id, city_name, county_name, region |
| `dim_date` | 日期維度 | date_id, full_date, year, month, day_of_week |
| `dim_influencer` | 網紅資訊 | influencer_id, name, platform, follower_count |

### Fact Table（事實表）

| 資料表 | 說明 | 主要欄位 |
|--------|------|----------|
| `fact_user_like` | 按讚事件記錄 | like_id, user_id(FK), post_id(FK), tag_id(FK), location_id(FK), date_id(FK), like_count |

### 關聯表

| 資料表 | 說明 |
|--------|------|
| `post_tag` | 貼文與標籤的多對多關聯 |
| `user_interest_tag` | 用戶興趣標籤（含 weight 權重欄位） |

## 功能需求

### 核心功能（必做）

**Feature 1：粉絲居住地分佈分析**
- 查詢指定網紅的所有粉絲，統計各縣市的粉絲數量
- 按粉絲數量由高到低排名，回傳 Top N 縣市
- 回傳結果包含：縣市名、粉絲數、佔比百分比

**Feature 2：活動地點推薦**
- 根據粉絲分佈結果，推薦最適合舉辦活動的縣市
- 可選：結合興趣標籤過濾（例如只看「美食」相關粉絲的分佈）
- 回傳推薦結果包含：推薦縣市、推薦理由、信心分數

**Feature 3：Star Schema 聚合查詢**
- 實作基於 Star Schema 的聚合查詢 API
- 支援多維度分析：依縣市、依標籤、依日期範圍
- 使用 Spring Data JPA 或 native SQL 實作

### 加分功能（Bonus）

| 功能 | 說明 |
|------|------|
| 用戶興趣標籤自動標記 | 按讚時自動擷取貼文 tag，更新用戶興趣標籤表（weight 隨按讚次數增加） |
| 主題化地點推薦 | 結合 tag 與 location 交叉分析（例如：喜歡美食的粉絲主要住在哪裡） |
| 前端視覺化儀表板 | 使用 Chart.js / ECharts 製作圖表（圓餅圖、熱度圖等） |

## API 端點設計

| Method | Endpoint | 說明 |
|--------|----------|------|
| GET | `/api/influencers/{id}/followers/distribution` | 查詢網紅粉絲的縣市分佈 |
| GET | `/api/influencers/{id}/recommend-city` | 推薦最佳活動舉辦縣市 |
| POST | `/api/users/{id}/likes` | 記錄用戶按讚貼文（觸發 tag 更新） |
| GET | `/api/users/{id}/tags` | 查詢用戶的興趣標籤 |
| GET | `/api/tags/{tagId}/city-ranking` | 依標籤查詢縣市排名 |
| GET | `/api/events/suggest?influencerId=X&tag=Y` | 推薦活動地點與主題 |
| GET | `/api/analytics/star-schema/city-summary` | Star Schema 聚合：依縣市 |
| GET | `/api/analytics/star-schema/tag-summary` | Star Schema 聚合：依標籤 |

## 必要要求

### 一、Swagger API 文件（必須完成）

每個 API 端點都**必須**搭配 Swagger 文件。

**步驟 1 — 加入依賴**（在 `pom.xml` 中加入）：
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.6</version>
</dependency>
```

**步驟 2 — 在 Controller 加上註解**：
```java
@Tag(name = "網紅管理", description = "網紅相關的 API")
@RestController
@RequestMapping("/api/influencers")
public class InfluencerController {

    @Operation(summary = "查詢粉絲分佈", description = "取得指定網紅粉絲的縣市分佈統計")
    @GetMapping("/{id}/followers/distribution")
    public ResponseEntity<?> getFollowerDistribution(@PathVariable Long id) {
        // ...
    }
}
```

**步驟 3 — 在 DTO 加上欄位說明**：
```java
public class FollowerDistributionResponse {
    @Schema(description = "縣市名稱", example = "台北市")
    private String city;

    @Schema(description = "粉絲數量", example = "18000")
    private Integer count;

    @Schema(description = "佔比百分比", example = "36.0")
    private Double percentage;
}
```

**存取路徑**（啟動後）：
- Swagger UI：`http://localhost:8080/swagger-ui.html`
- OpenAPI JSON：`http://localhost:8080/v3/api-docs`

**驗收標準**：
- 所有 API 端點都能在 Swagger UI 上正確顯示並可測試
- 每個端點與欄位都有清楚的中文說明

### 二、Postman API 測試（必須完成）

所有 API 端點都**必須**使用 Postman 建立測試案例，並匯出為 Collection 一併繳交。

#### 為什麼用 Postman？

在實際工作中，後端工程師開發完 API 後第一件事就是「用 Postman 打打看」。Postman 讓你可以：
- 快速驗證 API 是否能正確回應
- 模擬前端的各種請求（正常參數、異常參數、邊界值）
- 把測試案例存起來，下次改了 code 可以一鍵重新測試
- 匯出 Collection 分享給團隊成員

#### 建立 Collection 的要求

**1. Collection 結構**

在 Postman 中建立一個名為 `webredgood` 的 Collection，按照功能分資料夾：

```
📁 webredgood
├── 📁 Influencer API
│   ├── 查詢粉絲分佈 - 正常情境
│   ├── 查詢粉絲分佈 - 網紅不存在
│   ├── 推薦活動地點 - 正常情境
│   └── 推薦活動地點 - 帶 Tag 過濾
├── 📁 User API
│   ├── 用戶按讚貼文 - 正常情境
│   ├── 用戶按讚貼文 - 重複按讚
│   └── 查詢用戶興趣標籤
├── 📁 Analytics API
│   ├── 縣市聚合查詢
│   └── 標籤聚合查詢
└── 📁 Event API
    ├── 推薦活動地點與主題 - 正常情境
    └── 推薦活動地點與主題 - 無資料情境
```

**2. 每個請求必須包含**

- **正確的 HTTP Method 和 URL**
- **必要的 Headers**（例如 `Content-Type: application/json`）
- **Request Body**（POST 請求要有範例 JSON）
- **描述說明**（這個測試在測什麼）

**3. 範例：POST 按讚請求**

```
Method: POST
URL: http://localhost:8080/api/users/1/likes
Headers:
  Content-Type: application/json
Body (raw JSON):
{
    "postId": 15,
    "userId": 1
}
```

**4. 範例：GET 粉絲分佈請求**

```
Method: GET
URL: http://localhost:8080/api/influencers/1/followers/distribution
```

預期回傳：
```json
{
    "influencer": "小明",
    "totalFollowers": 50000,
    "distribution": [
        { "city": "台北市", "count": 18000, "percentage": 36.0 },
        { "city": "台中市", "count": 9500, "percentage": 19.0 },
        { "city": "高雄市", "count": 7000, "percentage": 14.0 }
    ]
}
```

#### 測試情境要求

每個 API 至少要建立以下測試情境：

| 情境類型 | 說明 | 範例 |
|----------|------|------|
| **正常情境 (Happy Path)** | 正確的參數，預期正常回應 | 查詢存在的網紅粉絲分佈 → 200 OK |
| **資料不存在** | 查詢不存在的資源 | 查詢 id=9999 的網紅 → 404 Not Found |
| **參數錯誤** | 傳入不合法的參數 | id 傳入負數或文字 → 400 Bad Request |
| **邊界值** | 測試資料邊界情況 | 網紅沒有任何粉絲 → 回傳空陣列 |

#### Postman Tests（自動驗證）

在 Postman 的 **Tests** 分頁中加入自動驗證腳本，這樣執行時會自動檢查結果：

```javascript
// 驗證 HTTP 狀態碼
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// 驗證回傳格式
pm.test("Response has distribution array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("distribution");
    pm.expect(jsonData.distribution).to.be.an("array");
});

// 驗證資料正確性
pm.test("Distribution sorted by count descending", function () {
    var jsonData = pm.response.json();
    var dist = jsonData.distribution;
    for (var i = 1; i < dist.length; i++) {
        pm.expect(dist[i-1].count).to.be.at.least(dist[i].count);
    }
});

// 驗證百分比總和
pm.test("Percentages sum to approximately 100", function () {
    var jsonData = pm.response.json();
    var total = jsonData.distribution.reduce((sum, d) => sum + d.percentage, 0);
    pm.expect(total).to.be.within(99, 101);
});
```

#### 匯出方式

完成所有測試後，匯出 Collection：
1. 在 Collection 名稱上點右鍵 → **Export**
2. 選擇 **Collection v2.1** 格式
3. 儲存為 `webredgood.postman_collection.json`
4. 放到專案根目錄一併繳交

---

### 三、Log 日誌（必須完成）

> 詳見上方「重要開發技能：Log 與 Debug」章節。

**驗收標準：**
- 每個 Service 方法都有 `log.info()` 記錄方法的進入與結束
- 重要的中間計算結果使用 `log.debug()` 記錄
- 所有的例外處理都有 `log.error()` 或 `log.warn()` 記錄
- `application.properties` 有正確設定 Log 等級與輸出格式
- **不得出現任何 `System.out.println()`**

---

## 專案結構

```
webredgood/
├── src/
│   └── main/
│       ├── java/com/aiinpocket/webredgood/
│       │   ├── config/          # 設定類別（Swagger 等）
│       │   ├── controller/      # REST API 控制器
│       │   │   ├── InfluencerController.java
│       │   │   ├── UserController.java
│       │   │   ├── AnalyticsController.java
│       │   │   └── EventController.java
│       │   ├── dto/             # 資料傳輸物件（Request / Response）
│       │   ├── entity/          # JPA 實體類別
│       │   ├── enums/           # 列舉型別
│       │   ├── exception/       # 自定義例外處理
│       │   ├── repository/      # 資料存取介面
│       │   ├── service/         # 商業邏輯層
│       │   │   ├── RecommendationService.java
│       │   │   ├── TaggingService.java
│       │   │   └── AnalyticsService.java
│       │   └── WebredgoodApplication.java
│       └── resources/
│           ├── application.properties
│           ├── schema.sql       # 資料表建立腳本（若不用 JPA 自動建表）
│           └── data.sql         # 初始測試資料
├── webredgood.postman_collection.json   # Postman 測試集合
├── pom.xml
└── mvnw
```

## 開發環境建置

### 前置需求

- JDK 21
- Maven（專案已內建 mvnw）
- PostgreSQL（建議使用 Docker）
- IDE：IntelliJ IDEA
- Postman（API 測試工具）

### 啟動 PostgreSQL（Docker）

```bash
docker run -d \
  --name webredgood-db \
  -e POSTGRES_DB=webredgood \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16
```

### 啟動專案

```bash
cd webredgood
./mvnw spring-boot:run
```

### 開發流程建議

```
1. 寫完一個 API
2. 先看 Console Log 有沒有啟動錯誤
3. 開 Swagger UI 確認 API 有正確顯示
4. 開 Postman 建立測試請求並執行
5. 如果結果不對 → 看 Log → 設中斷點 → Debug
6. 修正後再用 Postman 重新測試
7. 確認通過後，把 Postman Tests 腳本寫好
8. 進入下一個 API 的開發
```

## 繳交要求

- [ ] 完整的 Spring Boot 專案原始碼（含 pom.xml）
- [ ] README.md：包含啟動方式、資料庫設定、API 使用說明
- [ ] 資料庫初始化腳本（data.sql）：至少包含 **50 位用戶、5 位網紅、30 篇貼文、10 個 Tag**
- [ ] 星狀圖設計文件（ER Diagram 或文字說明）
- [ ] Swagger UI 可正常運行且所有 API 可測試
- [ ] Postman Collection 匯出檔（`webredgood.postman_collection.json`）
- [ ] 每個 API 至少有正常情境 + 異常情境兩個 Postman 測試
- [ ] 所有 Service 方法都有完整的 Log 記錄（不得使用 `System.out.println()`）
