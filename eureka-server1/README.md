# MAVEN
* mvn clean install
# DOCKER
## image
docker build -t eureka-server1 .
## container
| `-e` 指定變數，application.properties裡使用${}，如${DB_URL}
* docker run -e DB_URL={host:port} -p 8080:8080 ordermanager
## 取出docker container 裡的檔案，例如 log
docker cp :/file/path/within/container /host/path/target
