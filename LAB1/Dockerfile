FROM debian:latest

RUN apt-get update && \
    apt-get install -y apache2 \
    wget \
    openjdk-17-jre && \
    apt-get clean && \
    wget http://archive.ubuntu.com/ubuntu/pool/multiverse/liba/libapache-mod-fastcgi/libapache2-mod-fastcgi_2.4.7~0910052141-1.2_amd64.deb && \
    dpkg -i libapache2-mod-fastcgi_2.4.7~0910052141-1.2_amd64.deb && \
    mkdir -p /var/lib/apache2/fastcgi && \
    chown www-data:www-data /var/lib/apache2/fastcgi && \
    chown www-data:www-data /var/log/apache2

 COPY ./apache/httpd.conf /etc/apache2/apache2.conf
 COPY ./front/index.html /var/www/html
# # COPY ./fcgi/build/libs/fcgi.jar /var/lib/apache2/fastcgi/fcgi.jar

# # COPY ./entrypoint.sh /usr/local/bin/entrypoint.sh
# RUN chmod +x /usr/local/bin/entrypoint.sh