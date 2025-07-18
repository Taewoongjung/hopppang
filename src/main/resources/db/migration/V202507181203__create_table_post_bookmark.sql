CREATE TABLE post_bookmark
(
    post_id         BIGINT UNSIGNED            NOT NULL COMMENT '북마크 대상 게시글의 id',
    user_id         BIGINT UNSIGNED            NOT NULL COMMENT '북마크 누른 유저의 id',
    clicked_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL COMMENT '북마크 누른 날짜 및 시간',
    PRIMARY KEY (post_id, user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '게시판 북마크';