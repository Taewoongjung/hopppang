CREATE TABLE user_address
(
    id              BIGINT(20) UNSIGNED      NOT NULL AUTO_INCREMENT,
    user_id         BIGINT(20) UNSIGNED      NOT NULL COMMENT '유저 id',
    address         VARCHAR(50)              NOT NULL COMMENT '메인 주소',
    sub_address     VARCHAR(50)              NOT NULL COMMENT '부주소',
    building_number VARCHAR(50)              NOT NULL COMMENT '빌딩번호',
    created_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '유저 주소 정보';

CREATE INDEX `idx-user_address-user_id`
    ON user_address (user_id);