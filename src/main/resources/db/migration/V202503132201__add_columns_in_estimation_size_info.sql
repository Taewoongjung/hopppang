ALTER TABLE chassis_estimation_size_info
    ADD COLUMN chassis_discount_event_id BIGINT(20) UNSIGNED NULL COMMENT '샤시 할인 이벤트의 id' AFTER price;

ALTER TABLE chassis_estimation_size_info
    ADD COLUMN discounted_price INT UNSIGNED NULL COMMENT '할인 적용 된 샤시 가격' AFTER chassis_discount_event_id;
