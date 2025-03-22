ALTER TABLE chassis_discount_event
    ADD COLUMN discount_type CHAR(12) NULL COMMENT '할인 유형 (비율 or 고정 금액)' AFTER discount_rate;

UPDATE chassis_discount_event
SET discount_type = 'PERCENTAGE';

ALTER TABLE chassis_discount_event
    MODIFY COLUMN discount_type CHAR(12) NOT NULL COMMENT '할인 유형 (비율 or 고정 금액)';



ALTER TABLE chassis_estimation_info
    ADD COLUMN chassis_discount_event_id BIGINT(20) UNSIGNED NULL COMMENT '샤시 할인 이벤트의 id (고정 금액 할인만 적용 됨)' AFTER total_price;
ALTER TABLE chassis_estimation_info
    ADD COLUMN discounted_total_price INT UNSIGNED NULL COMMENT '할인 된 총 합계 가격' AFTER chassis_discount_event_id;
