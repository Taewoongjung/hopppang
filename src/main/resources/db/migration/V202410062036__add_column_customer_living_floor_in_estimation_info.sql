ALTER TABLE chassis_estimation_info
    ADD COLUMN customer_living_floor INT NOT NULL COMMENT '고객이 거주하고 있는 층수' AFTER total_price;