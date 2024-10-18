ALTER TABLE user
    ADD COLUMN required_re_login CHAR(1) DEFAULT F NOT NULL COMMENT '다시 로그인이 필요한 유저(리프레시, 엑세스 토큰 둘 다 만료 되어서.)' AFTER oauth_type;