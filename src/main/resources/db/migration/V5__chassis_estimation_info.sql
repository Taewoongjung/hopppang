CREATE TABLE chassis_estimation_info
(
    id                              BIGINT(20) UNSIGNED      NOT NULL AUTO_INCREMENT,
    user_id                         BIGINT(20) UNSIGNED          NULL COMMENT '유저 id',
    chassis_estimation_address_id   BIGINT(20) UNSIGNED      NOT NULL COMMENT '샤시 견적에 해당하는 주소',
    width                           INT UNSIGNED             NOT NULL COMMENT '너비(w)',
    height                          INT UNSIGNED             NOT NULL COMMENT '높이(h)',
    labor_fee                       INT UNSIGNED             NOT NULL COMMENT '인건비',
    ladder_car_fee                  INT UNSIGNED             NOT NULL COMMENT '사다리차 비용',
    demolition_fee                  INT UNSIGNED             NOT NULL COMMENT '철거 비용',
    maintenance_fee                 INT UNSIGNED             NOT NULL COMMENT '보양 비용',
    freight_transport_fee           INT UNSIGNED             NOT NULL COMMENT '도수운반 비용',
    delivery_fee                    INT UNSIGNED             NOT NULL COMMENT '배송 비용',
    price                           INT UNSIGNED             NOT NULL COMMENT '총 합계 가격',
    created_at                      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (chassis_estimation_address_id) REFERENCES chassis_estimation_address(id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '샤시 견적 정보';

CREATE INDEX `idx-chassis_estimation_info-user_id`
    ON chassis_estimation_info (user_id);