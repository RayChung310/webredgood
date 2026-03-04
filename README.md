# 網紅活動地點推薦系統（webredgood）

利用粉絲的居住地與興趣標籤，為網紅推薦適合舉辦實體活動的城市與主題的後端服務。

---

### 前置需求

- **JDK 21**
- **Maven**（或使用 `./mvnw`）
- **PostgreSQL**（ 16+）

### 資料庫設定

1. 建立資料庫：
   ```
   createdb webredgood
   ```
2. 連線設定在 `src/main/resources/application.properties`，設定url、username、password即可。

### 啟動方式

**Windows：**
```bash
.\mvnw.cmd spring-boot:run
```

**Mac / Linux：**
```bash
./mvnw spring-boot:run
```

預設為 **http://localhost:8080**。

## 專案介紹

本專案是一個以資料分析為核心的 Spring Boot 後端系統，透過 Star Schema（星狀模型）與粉絲按讚行為，提供：

- 粉絲縣市分佈分析
- 活動舉辦地點推薦
- 用戶興趣標籤管理
- 依標籤與城市的交叉分析
- 依城市 / 標籤的聚合報表 API


---

## 功能一覽

- **Influencer（網紅）**
  - 查詢粉絲縣市分佈
  - 推薦活動舉辦城市（可選 tag 過濾）

- **User（用戶）**
  - 紀錄用戶對貼文的按讚行為
  - 依按讚自動更新用戶興趣標籤權重
  - 查詢用戶興趣標籤清單

- **Tag（標籤）**
  - 依指定標籤統計粉絲主要分佈城市（城市排名）

- **Event（活動推薦）**
  - 綜合粉絲城市與興趣，推薦活動舉辦地點與主題

- **Analytics（分析 / 報表）**
  - 依城市的互動聚合
  - 依標籤的互動聚合

---

## 技術棧

- Java 21
- Spring Boot 3（Spring Web MVC）
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- Springdoc OpenAPI（Swagger UI）
- Maven / mvnw
- Postman（API 測試）

---

## 系統架構與資料模型

系統以 Star Schema 規劃分析資料：

- **維度表**
  - `dim_user`：用戶（含城市資訊）
  - `dim_post`：貼文
  - `dim_tag`：興趣標籤
  - `dim_location`：城市 / 行政區 / 經緯度
  - `dim_date`：日期（年月日、週幾、季別）
  - `dim_influencer`：網紅基本資料

- **事實表**
  - `fact_user_like`：用戶按讚紀錄（user / post / tag / location / date / like_count）

- **關聯表**
  - `post_tag`：貼文與標籤多對多
  - `user_interest_tag`：用戶興趣標籤＋權重

所有建表與初始化測試資料定義於 `src/main/resources/data.sql`。

---

## 專案結構概觀

```
webredgood/
├── src/
│   └── main/
│       ├── java/com/aiinpocket/webredgood/
│       │   ├── config/        # Swagger 等設定
│       │   ├── controller/    # REST API 控制器
│       │   ├── dto/           # Request / Response DTO
│       │   ├── entity/        # JPA 實體（Star Schema）
│       │   ├── repository/    # Spring Data JPA 介面
│       │   ├── service/       # 商業邏輯
│       │   └── WebredgoodApplication.java
│       └── resources/
│           ├── application.properties
│           └── data.sql
├── pom.xml
└── README.md

```
