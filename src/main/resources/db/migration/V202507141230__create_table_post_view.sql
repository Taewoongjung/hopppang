CREATE TABLE post_view
(
    post_id         BIGINT UNSIGNED            NOT NULL COMMENT '조회 한 게시글의 id',
    user_id         BIGINT UNSIGNED                NULL COMMENT '조회 한 유저의 id',
    clicked_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL COMMENT '조회 날짜 및 시간'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '게시판 조회';