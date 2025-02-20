CREATE TABLE user_login_history
(
    id            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id       BIGINT(20) UNSIGNED NOT NULL COMMENT '유저 id',
    created_at    DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '유저 로그인 히스토리 정보';

CREATE INDEX `idx-user_login_history-user_id`
    ON user_login_history (user_id);