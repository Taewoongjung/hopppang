FROM nginx:latest

# 필요한 빌드 도구 설치
RUN apt-get update && apt-get install -y wget build-essential libpcre3 libpcre3-dev zlib1g zlib1g-dev libssl-dev git

# Nginx 소스 코드 다운로드
RUN wget http://nginx.org/download/nginx-$(nginx -v 2>&1 | sed 's/^nginx version: nginx\///').tar.gz && \
    tar -zxvf nginx-*.tar.gz && \
    rm nginx-*.tar.gz

# nginx_upstream_check_module 다운로드
RUN git clone https://github.com/yaoweibin/nginx_upstream_check_module.git

# Nginx 재컴파일
RUN cd nginx-* && \
    patch -p1 < ../nginx_upstream_check_module/check_1.20.1+.patch && \
    ./configure --with-compat --add-dynamic-module=../nginx_upstream_check_module && \
    make modules

# 컴파일된 모듈을 Nginx 모듈 디렉토리로 복사
RUN cp nginx-*/objs/ngx_http_upstream_check_module.so /etc/nginx/modules/

# 불필요한 파일 삭제
RUN rm -rf nginx-* nginx_upstream_check_module

# Nginx 설정 파일에 모듈 로드 지시어 추가
RUN echo "load_module modules/ngx_http_upstream_check_module.so;" > /etc/nginx/modules-enabled/50-mod-http-upstream-check.conf

# 기본 CMD 유지
CMD ["nginx", "-g", "daemon off;"]