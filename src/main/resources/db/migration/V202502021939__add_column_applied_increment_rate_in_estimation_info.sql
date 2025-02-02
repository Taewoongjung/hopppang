ALTER TABLE chassis_estimation_info
    ADD COLUMN applied_increment_rate INT NOT NULL DEFAULT 0 COMMENT '원가 대비 총 가격 상승 비율' AFTER delivery_fee;