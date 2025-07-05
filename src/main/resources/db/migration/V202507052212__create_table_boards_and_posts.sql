CREATE TABLE boards
(
    id              BIGINT UNSIGNED            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(20)                NOT NULL COMMENT '종류 명',
    is_available    CHAR(1)                    NOT NULL COMMENT '카테고리 활성화 여부',
    created_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '게시판 종류';


CREATE TABLE posts
(
    id              BIGINT UNSIGNED            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    board_id        BIGINT UNSIGNED            NOT NULL COMMENT '게시판 종류의 id',
    register_id     BIGINT UNSIGNED            NOT NULL COMMENT '등록한 유저의 id',
    title           VARCHAR(100)               NOT NULL COMMENT '게시판 제목',
    contents        VARCHAR(1000)              NOT NULL COMMENT '게시판 내용',
    is_anonymous    CHAR(1)                    NOT NULL COMMENT '익명 질문 여부',
    created_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '게시판';

CREATE INDEX `idx-posts-board_id`
    ON posts (board_id);
