-- 제약 조건 삭제
ALTER TABLE user
    DROP CONSTRAINT unique_email_oauth_type;

-- 새로운 유니크 제약 조건 추가
ALTER TABLE user
    ADD CONSTRAINT unique_email_oauth_type_device_id UNIQUE (email, oauth_type, device_id);
