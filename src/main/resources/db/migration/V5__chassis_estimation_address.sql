CREATE TABLE chassis_estimation_address
(
    id              BIGINT(20) UNSIGNED      NOT NULL AUTO_INCREMENT,
    zip_code        VARCHAR(6)               NOT NULL COMMENT '우편번호',
    address         VARCHAR(50)              NOT NULL COMMENT '메인 주소',
    sub_address     VARCHAR(50)              NOT NULL COMMENT '부주소',
    building_number VARCHAR(50)              NOT NULL COMMENT '빌딩번호',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '샤시 견적에 대한 주소 정보';
