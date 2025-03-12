CREATE TABLE advertisement_content
(
    id                               BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    adv_id                           VARCHAR(50)         NOT NULL COMMENT '광고 유니크 값',
    adv_channel                      VARCHAR(50)         NOT NULL COMMENT '광고 노출 장소',
    start_at                         DATETIME(6)             NULL COMMENT '광고 시작 시간',
    end_at                           DATETIME(6)             NULL COMMENT '광고 종료 시간',
    publisher_id                     BIGINT(20) UNSIGNED NOT NULL COMMENT '광고 발행인',
    memo                             VARCHAR(700)            NULL COMMENT '메모',
    created_at                       DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified                    DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '광고 컨텐츠 정보';

CREATE INDEX `idx-advertisement_content-ads_id`
    ON advertisement_content (adv_id);

CREATE INDEX `idx-advertisement_content-adv_channel`
    ON advertisement_content (adv_channel);