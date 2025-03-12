CREATE TABLE user_traffic_source
(
    id                               BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    advertisement_content_id         BIGINT(20) UNSIGNED NOT NULL COMMENT '광고컨텐츠 id',
    entry_page_type                  CHAR(10)            NOT NULL COMMENT '광고 한 인입 페이지',
    referrer                         VARCHAR(700)            NULL COMMENT '유입 경로',
    browser                          VARCHAR(30)             NULL COMMENT '유저의 브라우저 종류',
    stay_duration                    INT                 NOT NULL COMMENT '체류 시간(초)',
    visited_at  DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '유저가 인입 되는 경로 정보';

CREATE INDEX `idx-user_traffic_source-advertisement_content_id`
    ON user_traffic_source (advertisement_content_id);

CREATE INDEX `idx-user_traffic_source-entry_page_type`
    ON user_traffic_source (entry_page_type);

CREATE INDEX `idx-user_traffic_source-referrer`
    ON user_traffic_source (referrer);