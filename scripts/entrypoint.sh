#!/bin/bash
export SERVER_ID=$(hostname)
exec java -jar -Djava.security.egd=file:/dev/./urandom \
     -Dsun.net.inetaddr.ttl=0 \
     -Dspring.profiles.active=prod \
     app.jar