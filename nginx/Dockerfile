FROM nginx:latest

# Install build dependencies
RUN apt-get update && apt-get install -y wget build-essential libpcre3 libpcre3-dev zlib1g zlib1g-dev libssl-dev git

# Get Nginx version
RUN NGINX_VERSION=$(nginx -v 2>&1 | sed 's/^nginx version: nginx\///')

# Download Nginx source code
RUN wget http://nginx.org/download/nginx-${NGINX_VERSION}.tar.gz && \
    tar -zxvf nginx-${NGINX_VERSION}.tar.gz && \
    rm nginx-${NGINX_VERSION}.tar.gz

# Clone nginx_upstream_check_module
RUN git clone https://github.com/yaoweibin/nginx_upstream_check_module.git

# Compile Nginx with the module
RUN cd nginx-${NGINX_VERSION} && \
    patch -p1 < ../nginx_upstream_check_module/check_1.20.1+.patch && \
    ./configure --with-compat --add-dynamic-module=../nginx_upstream_check_module && \
    make modules && \
    if [ ! -f objs/ngx_http_upstream_check_module.so ]; then \
        echo "Module compilation failed" && exit 1; \
    fi

# Copy the compiled module to Nginx modules directory
RUN mkdir -p /etc/nginx/modules && \
    cp nginx-${NGINX_VERSION}/objs/ngx_http_upstream_check_module.so /etc/nginx/modules/

# Clean up
RUN rm -rf nginx-${NGINX_VERSION} nginx_upstream_check_module

# Add module loading to Nginx configuration
RUN echo "load_module modules/ngx_http_upstream_check_module.so;" > /etc/nginx/modules-enabled/50-mod-http-upstream-check.conf

# Keep the default CMD
CMD ["nginx", "-g", "daemon off;"]