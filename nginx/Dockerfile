FROM nginx:latest

# Install build dependencies
RUN apt-get update && apt-get install -y wget build-essential libpcre3 libpcre3-dev zlib1g zlib1g-dev libssl-dev git

# Set Nginx version explicitly (change this to match your base image version)
ENV NGINX_VERSION 1.25.3

# Download Nginx source code
RUN wget http://nginx.org/download/nginx-${NGINX_VERSION}.tar.gz && \
    tar -zxvf nginx-${NGINX_VERSION}.tar.gz && \
    rm nginx-${NGINX_VERSION}.tar.gz

# Clone nginx_upstream_check_module
RUN git clone https://github.com/yaoweibin/nginx_upstream_check_module.git

# Compile Nginx with the module
RUN cd nginx-${NGINX_VERSION} && \
    patch -p1 < ../nginx_upstream_check_module/check_1.20.1+.patch && \
    ./configure --prefix=/etc/nginx --sbin-path=/usr/sbin/nginx --modules-path=/usr/lib/nginx/modules \
    --conf-path=/etc/nginx/nginx.conf --error-log-path=/var/log/nginx/error.log \
    --http-log-path=/var/log/nginx/access.log --pid-path=/var/run/nginx.pid \
    --lock-path=/var/run/nginx.lock --with-compat --with-threads --with-http_ssl_module \
    --with-http_v2_module --with-http_realip_module --with-http_addition_module \
    --with-http_sub_module --with-http_dav_module --with-http_flv_module \
    --with-http_mp4_module --with-http_gunzip_module --with-http_gzip_static_module \
    --with-http_random_index_module --with-http_secure_link_module --with-http_stub_status_module \
    --with-http_auth_request_module --with-mail --with-mail_ssl_module --with-stream \
    --with-stream_ssl_module --add-dynamic-module=../nginx_upstream_check_module && \
    make modules && \
    if [ ! -f objs/ngx_http_upstream_check_module.so ]; then \
        echo "Module compilation failed" && exit 1; \
    fi

# Copy the compiled module to Nginx modules directory
RUN mkdir -p /usr/lib/nginx/modules && \
    cp nginx-${NGINX_VERSION}/objs/ngx_http_upstream_check_module.so /usr/lib/nginx/modules/

# Clean up
RUN rm -rf nginx-${NGINX_VERSION} nginx_upstream_check_module

# Add module loading to Nginx configuration
RUN echo "load_module modules/ngx_http_upstream_check_module.so;" > /etc/nginx/modules-enabled/50-mod-http-upstream-check.conf

# Keep the default CMD
CMD ["nginx", "-g", "daemon off;"]