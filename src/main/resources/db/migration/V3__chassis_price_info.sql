CREATE TABLE chassis_price_info
(
    company_type    VARCHAR(20)              NOT NULL COMMENT '회사 종류',
    type            VARCHAR(20)              NOT NULL COMMENT '샤시 종류',
    width           INT     UNSIGNED         NOT NULL COMMENT '샤시의 너비',
    height          INT     UNSIGNED         NOT NULL COMMENT '샤시의 높이',
    price           INT     UNSIGNED         NOT NULL COMMENT '해당 샤시의 종류, 너비, 높이에 따른 가격',
    last_modified   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (company_type, type, width, height)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '샤시 가격 정보 (해당 가격 정보로 계산 함)';

CREATE INDEX `idx-chassis_price_info-company_type`
    ON chassis_price_info (company_type);