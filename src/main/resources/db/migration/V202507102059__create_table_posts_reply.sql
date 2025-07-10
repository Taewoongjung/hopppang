CREATE TABLE posts_reply
(
    id              BIGINT UNSIGNED            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    post_id         BIGINT UNSIGNED            NOT NULL COMMENT '게시판 id',
    root_reply_id   BIGINT UNSIGNED                NULL COMMENT '루트 댓글의 id',
    contents        VARCHAR(1000)              NOT NULL COMMENT '댓글 내용',
    register_id     BIGINT UNSIGNED            NOT NULL COMMENT '등록한 유저의 id',
    is_anonymous    CHAR(1)                    NOT NULL COMMENT '익명 댓글 여부',
    is_deleted      CHAR(1)                    NOT NULL COMMENT '삭제 여부',
    created_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '게시판 댓글';

CREATE INDEX `idx-posts_reply-post_id`
    ON posts_reply (post_id);