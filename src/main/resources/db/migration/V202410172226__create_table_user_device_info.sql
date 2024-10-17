CREATE TABLE user_device_info
(
    id                              BIGINT(20) UNSIGNED      NOT NULL AUTO_INCREMENT,
    user_id                         BIGINT(20) UNSIGNED      NOT NULL COMMENT '유저 id',
    device_type                     CHAR(10)                     NULL COMMENT '디바이스 종류',
    device_id                       VARCHAR(100)             NOT NULL COMMENT '디바이스 id',
    created_at                      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '유저 디바이스 정보';