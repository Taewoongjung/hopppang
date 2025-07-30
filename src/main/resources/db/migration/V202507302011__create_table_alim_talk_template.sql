CREATE TABLE alim_talk_template
(
    id                         BIGINT(20) UNSIGNED                      NOT NULL AUTO_INCREMENT,
    third_party_type           VARCHAR(20)                                  NULL COMMENT '해당 템플릿 등록 사이트(Aligo, bizM 등등..)',
    template_name              VARCHAR(50)                              NOT NULL COMMENT '템플릿 명',
    template_code              VARCHAR(20)                              NOT NULL COMMENT '템플릿 코드',
    message_subject            VARCHAR(30)                              NOT NULL COMMENT '제목',
    message                    VARCHAR(3000)                            NOT NULL COMMENT '내용',
    message_extra              VARCHAR(3000)                                NULL COMMENT '메시지 부가정보',
    replace_message_subject    VARCHAR(30)                              NOT NULL COMMENT '대체 제목',
    replace_message            VARCHAR(3000)                            NOT NULL COMMENT '대체 내용',
    button_link_type           CHAR(2)                                  NOT NULL COMMENT '버튼 링크 타입 (AL, WL, AC, DS, BK, MD 중에서 1개)',
    button_info                VARCHAR(700)                             NOT NULL COMMENT '버튼 정보',
    created_at                 DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    last_modified              DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '알림톡 발송 템플릿';
