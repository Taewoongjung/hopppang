CREATE TABLE user_config_info
(
    id                              BIGINT(20) UNSIGNED      NOT NULL AUTO_INCREMENT,
    user_id                         BIGINT(20) UNSIGNED      NOT NULL COMMENT '유저 id',
    is_push_on                      CHAR(1)                  NOT NULL COMMENT '푸시 메시지 on/off',
    created_at                      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '유저 설정 정보';