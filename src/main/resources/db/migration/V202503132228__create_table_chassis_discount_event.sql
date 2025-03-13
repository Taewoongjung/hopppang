CREATE TABLE chassis_discount_event
(
    id                              BIGINT(20) UNSIGNED      NOT NULL AUTO_INCREMENT,
    company_type                    VARCHAR(20)              NOT NULL COMMENT '할인 할 회사 종류',
    chassis_type                    VARCHAR(20)              NOT NULL COMMENT '할인 할 샤시 종류',
    started_at                      DATETIME(6)                  NULL COMMENT '할인 시작 날짜',
    ended_at                        DATETIME(6)                  NULL COMMENT '할인 종료 날짜',
    discount_rate                   INT UNSIGNED             NOT NULL COMMENT '할인율',
    is_still_on_discount            CHAR(1)                  NOT NULL COMMENT '할인 중 여부',
    publisher_id                    BIGINT(20) UNSIGNED      NOT NULL COMMENT '행사 주체자 id',
    created_at                      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '샤시 할인 이벤트 정보';

CREATE INDEX `idx-chassis_discount_event-company_type`
    ON chassis_discount_event (company_type);

CREATE INDEX `idx-chassis_discount_event-is_chassis_type_still_on_discount`
    ON chassis_discount_event (chassis_type, is_still_on_discount);