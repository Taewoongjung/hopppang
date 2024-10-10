-- 기존 유니크 제약 조건 삭제
ALTER TABLE user
DROP INDEX unique_email,
DROP INDEX unique_tel;

-- 새로운 유니크 제약 조건 추가
ALTER TABLE user
    ADD CONSTRAINT unique_email_oauth_type UNIQUE (email, oauth_type);

-- token 컬럼 삭제
ALTER TABLE user DROP COLUMN token;