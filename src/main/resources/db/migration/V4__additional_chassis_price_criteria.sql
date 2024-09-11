CREATE TABLE additional_chassis_price_criteria
(
    type            VARCHAR(20)              NOT NULL COMMENT '부가적인 필수값 타입',
    price           INT         UNSIGNED     NOT NULL COMMENT '가격',
    created_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '창호 가격에 반영 될 부가적인 필수값 테이블';