CREATE TABLE user_token_info
(
    id                              BIGINT(20) UNSIGNED      NOT NULL AUTO_INCREMENT,
    user_id                         BIGINT(20) UNSIGNED      NOT NULL COMMENT '유저 id',
    provider_user_id                VARCHAR(255)             NOT NULL COMMENT '각 oAuth 서비스 회사의 유저 식별 고유값',
    token_type                      CHAR(7)                  NOT NULL COMMENT '토큰 종류',
    token                           VARCHAR(255)             NOT NULL COMMENT '토큰 정보',
    connected_at                    DATETIME(6)              NOT NULL COMMENT 'oAuth 서비스에 연결 된 시간',
    expire_in                       DATETIME(6)              NOT NULL COMMENT '토큰 만료 시간',
    created_at                      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT unique_provider_user_id_token_type
        unique (provider_user_id, token_type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '유저 토큰 정보';