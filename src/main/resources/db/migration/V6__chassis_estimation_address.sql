CREATE TABLE chassis_estimation_address
(
    id                              BIGINT(20) UNSIGNED      NOT NULL AUTO_INCREMENT,
    chassis_estimation_info_id      BIGINT(20) UNSIGNED      NOT NULL COMMENT '샤시 견적 루트 정보 id',
    zip_code                        CHAR(5)                  NOT NULL COMMENT '우편번호',
    state                           VARCHAR(20)              NOT NULL COMMENT '시/도',
    city                            VARCHAR(20)              NOT NULL COMMENT '시/군/구',
    town                            VARCHAR(20)              NOT NULL COMMENT '읍/면/동',
    b_code                          CHAR(10)                 NOT NULL COMMENT '법정동코드',
    remain_address                  VARCHAR(70)              NOT NULL COMMENT '나머지 주소',
    building_number                 VARCHAR(50)              NOT NULL COMMENT '빌딩번호',
    is_apartment                    CHAR(1)                  NOT NULL COMMENT '아파트 여부',
    is_expanded                     CHAR(1)                  NOT NULL COMMENT '확장 여부',
    PRIMARY KEY (id),
    FOREIGN KEY (chassis_estimation_info_id) REFERENCES chassis_estimation_info(id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '샤시 견적에 대한 주소 정보';

CREATE INDEX `idx-chassis_estimation_address-chassis_estimation_info_id`
    ON chassis_estimation_address (chassis_estimation_info_id);

CREATE INDEX `idx-chassis_estimation_address-zip_code`
    ON chassis_estimation_address (zip_code);

CREATE INDEX `idx-chassis_estimation_address-city`
    ON chassis_estimation_address (city);

CREATE INDEX `idx-chassis_estimation_address-town`
    ON chassis_estimation_address (town);