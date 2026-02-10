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

### 二、測試案例（必須完成）

所有功能都**必須**撰寫測試。

**1. Service 層 — 單元測試**
```java
@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    @Test
    void should_回傳台北市為第一名_when_台北市粉絲最多() {
        // Given: 準備測試資料
        // When: 呼叫要測試的方法
        // Then: 驗證結果
    }
}
```

**2. Repository 層 — 整合測試**
```java
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_正確統計各縣市粉絲數() {
        // 測試自定義查詢方法
    }
}
```

**3. Controller 層 — 整合測試**
```java
@WebMvcTest(InfluencerController.class)
class InfluencerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;

    @Test
    void should_回傳200_when_查詢粉絲分佈() throws Exception {
        mockMvc.perform(get("/api/influencers/1/followers/distribution"))
               .andExpect(status().isOk());
    }
}
```

**測試命名規則**：`should_預期結果_when_條件()`

## 專案結構

```
webredgood/
├── src/
│   ├── main/
│   │   ├── java/com/aiinpocket/webredgood/
│   │   │   ├── config/          # 設定類別（Swagger 等）
│   │   │   ├── controller/      # REST API 控制器
│   │   │   │   ├── InfluencerController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── AnalyticsController.java
│   │   │   │   └── EventController.java
│   │   │   ├── dto/             # 資料傳輸物件（Request / Response）
│   │   │   ├── entity/          # JPA 實體類別
│   │   │   ├── enums/           # 列舉型別
│   │   │   ├── exception/       # 自定義例外處理
│   │   │   ├── repository/      # 資料存取介面
│   │   │   ├── service/         # 商業邏輯層
│   │   │   │   ├── RecommendationService.java
│   │   │   │   ├── TaggingService.java
│   │   │   │   └── AnalyticsService.java
│   │   │   └── WebredgoodApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── schema.sql       # 資料表建立腳本（若不用 JPA 自動建表）
│   │       └── data.sql         # 初始測試資料
│   └── test/
│       └── java/com/aiinpocket/webredgood/
│           ├── controller/      # Controller 測試
│           ├── service/         # Service 測試
│           └── repository/      # Repository 測試
├── pom.xml
└── mvnw
```

## 開發環境建置

### 前置需求

- JDK 21
- Maven（專案已內建 mvnw）
- PostgreSQL（建議使用 Docker）
- IDE：IntelliJ IDEA

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

### 執行測試

```bash
./mvnw test
```

## 繳交要求

- [ ] 完整的 Spring Boot 專案原始碼（含 pom.xml）
- [ ] README.md：包含啟動方式、資料庫設定、API 使用說明
- [ ] 資料庫初始化腳本（data.sql）：至少包含 **50 位用戶、5 位網紅、30 篇貼文、10 個 Tag**
- [ ] 星狀圖設計文件（ER Diagram 或文字說明）
- [ ] Swagger UI 可正常運行且所有 API 可測試
- [ ] 每個功能都有對應的測試案例
