ALTER TABLE user
    ADD COLUMN deleted_at DATETIME(6) NULL COMMENT '회원 탈퇴 한 시간' AFTER required_re_login;

-- 기존 unique 제약조건 삭제
ALTER TABLE user
DROP CONSTRAINT unique_email_oauth_type;

-- 새로운 unique 제약조건 추가
ALTER TABLE user
    ADD CONSTRAINT unique_email_oauth_type_deleted_at UNIQUE (email, oauth_type, deleted_at);