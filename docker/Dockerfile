FROM ubuntu
# Install the SQL server and other packages
RUN apt update && apt -y dist-upgrade && \
  apt install -y mariadb-server vim
RUN sed -i 's/= 127.0.0.1/= 0.0.0.0/' /etc/mysql/mariadb.conf.d/50-server.cnf
RUN /etc/init.d/mariadb start && \
  mysql -uroot -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'runpwd' WITH GRANT OPTION;" && \
  mysql -uroot -e "FLUSH PRIVILEGES;"
# Open the mariadb-server port
EXPOSE 3306
CMD /etc/init.d/mariadb start && while true; do sleep 6000; done
# Connect to the mariadb server with 'mysql -uroot -prunpwd -h 127.0.0.1
