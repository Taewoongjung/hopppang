ALTER TABLE user_config_info
    ADD COLUMN is_alim_talk_on CHAR(1) NULL COMMENT '알림톡 수신 여부 on/off' AFTER is_push_on;

UPDATE user_config_info
SET is_alim_talk_on = 'F';

ALTER TABLE user_config_info
    MODIFY COLUMN is_alim_talk_on CHAR(1) NOT NULL COMMENT '알림톡 수신 여부 on/off';