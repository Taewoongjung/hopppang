ALTER TABLE posts
    ADD COLUMN is_deleted CHAR(1) NOT NULL COMMENT '삭제 여부' AFTER is_anonymous;