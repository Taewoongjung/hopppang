CREATE TABLE post_reply_like
(
    post_reply_id   BIGINT UNSIGNED NOT NULL COMMENT '좋아요 대상 댓글의 id',
    user_id         BIGINT UNSIGNED NOT NULL COMMENT '좋아요 누른 유저의 id',
    clicked_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL COMMENT '좋아요 눌린 날짜 및 시간',
    PRIMARY KEY (post_reply_id, user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '게시판 댓글 좋아요';