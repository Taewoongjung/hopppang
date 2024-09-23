CREATE TABLE chassis_estimation_size_info
(
    id                              BIGINT(20) UNSIGNED      NOT NULL AUTO_INCREMENT,
    chassis_estimation_info_id      BIGINT(20) UNSIGNED      NOT NULL COMMENT '중요 부가적인 가격 정보 id',
    chassis_type                    VARCHAR(20)              NOT NULL COMMENT '샤시 종류',
    width                           INT UNSIGNED             NOT NULL COMMENT '너비(w)',
    height                          INT UNSIGNED             NOT NULL COMMENT '높이(h)',
    price                           INT UNSIGNED             NOT NULL COMMENT '측정 된 샤시 가격',
    created_at                      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    FOREIGN KEY (chassis_estimation_info_id) REFERENCES chassis_estimation_info(id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '샤시 가로 세로 길이에 대한 견적 정보';

CREATE INDEX `idx-chassis_estimation_size_info-chassis_estimation_info_id`
    ON chassis_estimation_size_info (chassis_estimation_info_id);