CREATE TABLE chassis_estimation_inquiry
(
    id                               BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id                          BIGINT(20) UNSIGNED NOT NULL COMMENT '견적 문의 요청 한 유저의 id',
    chassis_estimation_info_id       BIGINT(20) UNSIGNED NOT NULL COMMENT '문의 요청 된 견적의 id',
    created_at    DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '견적 문의 요청 정보';

CREATE INDEX `idx-chassis_estimation_inquiry-user_id`
    ON chassis_estimation_inquiry (user_id);