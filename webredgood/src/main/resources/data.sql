-- ==========  建立資料表（依規格：BIGINT、latitude/longitude、複合主鍵等） ==========
CREATE TABLE dim_tag (
                         tag_id BIGSERIAL PRIMARY KEY,
                         tag_name VARCHAR(100) UNIQUE NOT NULL,
                         category VARCHAR(50)
);

CREATE TABLE dim_influencer (
                                influencer_id BIGSERIAL PRIMARY KEY,
                                name VARCHAR(100) NOT NULL,
                                platform VARCHAR(50),
                                follower_count INT
);

CREATE TABLE dim_location (
                              location_id BIGSERIAL PRIMARY KEY,
                              city_name VARCHAR(50) NOT NULL,
                              county_name VARCHAR(50),
                              region VARCHAR(50),
                              latitude DECIMAL(10,7),
                              longitude DECIMAL(10,7)
);

CREATE TABLE dim_date (
                          date_id BIGSERIAL PRIMARY KEY,
                          full_date DATE UNIQUE NOT NULL,
                          year INT NOT NULL,
                          month INT NOT NULL,
                          day INT NOT NULL,
                          day_of_week VARCHAR(10),
                          quarter INT
);

CREATE TABLE dim_user (
                          user_id BIGSERIAL PRIMARY KEY,
                          username VARCHAR(100) NOT NULL,
                          email VARCHAR(255) UNIQUE,
                          city VARCHAR(50) NOT NULL,
                          county VARCHAR(50),
                          location_id BIGINT REFERENCES dim_location(location_id),
                          created_at TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE dim_post (
                          post_id BIGSERIAL PRIMARY KEY,
                          influencer_id BIGINT REFERENCES dim_influencer(influencer_id),
                          title VARCHAR(500) NOT NULL,
                          content TEXT,
                          created_at TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE post_tag (
                          post_id BIGINT REFERENCES dim_post(post_id),
                          tag_id BIGINT REFERENCES dim_tag(tag_id),
                          PRIMARY KEY (post_id, tag_id)
);

CREATE TABLE fact_user_like (
                                like_id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT REFERENCES dim_user(user_id),
                                post_id BIGINT REFERENCES dim_post(post_id),
                                tag_id BIGINT REFERENCES dim_tag(tag_id),
                                location_id BIGINT REFERENCES dim_location(location_id),
                                date_id BIGINT REFERENCES dim_date(date_id),
                                like_count INT DEFAULT 1
);

CREATE TABLE user_interest_tag (
                                   user_id BIGINT REFERENCES dim_user(user_id),
                                   tag_id BIGINT REFERENCES dim_tag(tag_id),
                                   weight DECIMAL(5,2) DEFAULT 1.0,
                                   last_updated TIMESTAMP,
                                   PRIMARY KEY (user_id, tag_id)
);

-- ==========  插入維度資料 ==========
INSERT INTO dim_tag (tag_name, category) VALUES
                                             ('美食', '生活'), ('旅遊', '生活'), ('科技', '專業'), ('健身', '健康'), ('美妝', '時尚'),
                                             ('遊戲', '娛樂'), ('穿搭', '時尚'), ('理財', '專業'), ('寵物', '生活'), ('攝影', '藝術');

INSERT INTO dim_influencer (name, platform, follower_count) VALUES
                                                                ('小明', 'YouTube', 10000), ('美美', 'Instagram', 20000), ('大華', 'Twitch', 5000),
                                                                ('玲玲', 'TikTok', 15000), ('阿強', 'Facebook', 8000);

INSERT INTO dim_location (city_name, county_name, region, latitude, longitude) VALUES
                                                                                   ('台北市', '信義區', '北部', 25.0336111, 121.5640278),
                                                                                   ('新北市', '板橋區', '北部', 25.0123456, 121.4635678),
                                                                                   ('台中市', '西區', '中部', 24.1477358, 120.6736452),
                                                                                   ('台南市', '中西區', '南部', 22.9997281, 120.2270281),
                                                                                   ('高雄市', '左營區', '南部', 22.6867892, 120.3014356),
                                                                                   ('桃園市', '桃園區', '北部', 24.9936281, 121.2969578),
                                                                                   ('新竹市', '東區', '北部', 24.8039452, 120.9646789),
                                                                                   ('花蓮縣', '花蓮市', '東部', 23.9769425, 121.6046789),
                                                                                   ('澎湖縣', '馬公市', '離島', 23.5694452, 119.5663456);

INSERT INTO dim_date (full_date, year, month, day, day_of_week, quarter)
SELECT
    d::date,
    EXTRACT(YEAR FROM d)::INT,
    EXTRACT(MONTH FROM d)::INT,
    EXTRACT(DAY FROM d)::INT,
    TRIM(TO_CHAR(d, 'Day')),
    EXTRACT(QUARTER FROM d)::INT
FROM generate_series('2026-01-01'::date, '2026-12-31'::date, '1 day'::interval) d;

-- ==========  插入 50 位用戶 ==========
INSERT INTO dim_user (username, email, city, county, location_id)
SELECT
    '用戶' || i,
    'user' || i || '@test.com',
    l.city_name,
    l.county_name,
    l.location_id
FROM generate_series(1, 50) i
JOIN LATERAL (
    SELECT *
    FROM dim_location
    ORDER BY location_id
    OFFSET ((i - 1) % (SELECT COUNT(*) FROM dim_location)) ROWS
    LIMIT 1
    ) AS l ON TRUE;


-- ==========  插入 30 篇貼文 ==========
INSERT INTO dim_post (influencer_id, title, content) VALUES
                                                         (1, '2026 必吃美食', '...'), (1, '科技新品開箱', '...'), (1, '台北一日遊', '...'), (1, '理財入門', '...'), (1, '寵物日常', '...'), (1, '攝影技巧', '...'),
                                                         (2, '春夏穿搭提案', '...'), (2, '美妝教學', '...'), (2, '健身菜單', '...'), (2, '旅遊 vlog', '...'), (2, '遊戲實況', '...'), (2, '穿搭週記', '...'),
                                                         (3, '遊戲精華', '...'), (3, '科技評論', '...'), (3, '旅遊攻略', '...'), (3, '美食探店', '...'), (3, '寵物開箱', '...'),
                                                         (4, '台南牛肉湯評比', '...'), (4, 'TikTok 趨勢', '...'), (4, '美妝新品', '...'), (4, '穿搭 OOTD', '...'), (4, '旅遊景點', '...'), (4, '美食地圖', '...'),
                                                         (5, '理財心得', '...'), (5, '健身紀錄', '...'), (5, '攝影作品', '...'), (5, '遊戲攻略', '...'), (5, '寵物飼養', '...'), (5, '生活分享', '...');

-- ==========  貼文與標籤關聯 (post_tag) ==========
INSERT INTO post_tag (post_id, tag_id) SELECT post_id, 1 FROM dim_post WHERE title LIKE '%美食%' OR title LIKE '%肉湯%' OR title LIKE '%吃%';
INSERT INTO post_tag (post_id, tag_id) SELECT post_id, 3 FROM dim_post WHERE title LIKE '%科技%' OR title LIKE '%開箱%' OR title LIKE '%評論%';
INSERT INTO post_tag (post_id, tag_id) SELECT post_id, 7 FROM dim_post WHERE title LIKE '%穿搭%' OR title LIKE '%OOTD%' OR title LIKE '%週記%';
INSERT INTO post_tag (post_id, tag_id) SELECT post_id, 4 FROM dim_post WHERE title LIKE '%健身%';
INSERT INTO post_tag (post_id, tag_id) SELECT post_id, 5 FROM dim_post WHERE title LIKE '%美妝%';
INSERT INTO post_tag (post_id, tag_id) SELECT post_id, 6 FROM dim_post WHERE title LIKE '%遊戲%';
INSERT INTO post_tag (post_id, tag_id) SELECT post_id, 2 FROM dim_post WHERE title LIKE '%旅遊%' OR title LIKE '%一日遊%' OR title LIKE '%vlog%' OR title LIKE '%景點%';
INSERT INTO post_tag (post_id, tag_id) SELECT post_id, 8 FROM dim_post WHERE title LIKE '%理財%';
INSERT INTO post_tag (post_id, tag_id) SELECT post_id, 9 FROM dim_post WHERE title LIKE '%寵物%';
INSERT INTO post_tag (post_id, tag_id) SELECT post_id, 10 FROM dim_post WHERE title LIKE '%攝影%';
INSERT INTO post_tag (post_id, tag_id)
SELECT p.post_id, 1 FROM dim_post p
WHERE NOT EXISTS (SELECT 1 FROM post_tag pt WHERE pt.post_id = p.post_id);

-- ==========  產生約 50 筆按讚 (fact_user_like) ==========
INSERT INTO fact_user_like (user_id, post_id, tag_id, location_id, date_id, like_count)
SELECT
    u.user_id,
    pt.post_id,
    pt.tag_id,
    u.location_id,
    d.date_id,
    1
FROM generate_series(1, 50) i
         JOIN LATERAL (
    SELECT user_id, location_id
    FROM dim_user
    ORDER BY user_id
    OFFSET ((i - 1) % (SELECT COUNT(*) FROM dim_user)) ROWS
    LIMIT 1
    ) AS u ON TRUE
    JOIN LATERAL (
    SELECT post_id, tag_id
    FROM post_tag
    ORDER BY post_id, tag_id
    OFFSET ((i - 1) % (SELECT COUNT(*) FROM post_tag)) ROWS
    LIMIT 1
    ) AS pt ON TRUE
    JOIN LATERAL (
    SELECT date_id
    FROM dim_date
    ORDER BY date_id
    OFFSET ((i - 1) % (SELECT COUNT(*) FROM dim_date)) ROWS
    LIMIT 1
    ) AS d ON TRUE;

-- ==========  用戶興趣權重 (user_interest_tag) ==========
INSERT INTO user_interest_tag (user_id, tag_id, weight, last_updated)
SELECT user_id, tag_id, COUNT(*)::DECIMAL(5,2), now()
FROM fact_user_like
GROUP BY user_id, tag_id;