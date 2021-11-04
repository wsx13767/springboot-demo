# MAVEN
* mvn clean install
# DOCKER
## image
docker build -t eureka-server:{version} .
## container
| `-e` 指定變數，application.properties裡使用${}，如${DB_URL}
| '-p' 指定port

* docker run -e SERVER_PORT=8761 -e EUREKA_HOSTNAME=localhost -e EUREKA_DEFAULT_ZONE=localhost:8761 -e APPLICATION_NAME=eureka-server -p 8761:8761
## 取出docker container 裡的檔案，例如 log
docker cp :/file/path/within/container /host/path/target
