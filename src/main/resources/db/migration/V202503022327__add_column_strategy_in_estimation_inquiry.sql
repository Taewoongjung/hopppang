ALTER TABLE chassis_estimation_inquiry
    ADD COLUMN strategy VARCHAR(20) NOT NULL DEFAULT '' COMMENT '문의 전략 (sns, tel 등)' AFTER chassis_estimation_info_id;