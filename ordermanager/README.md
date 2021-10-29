## MAVEN
* mvn clean install
## DOCKER
### image
* cd ./ordermanager-appliacation
* docker build -t ordermanager .
### container -e 指定變數
* docker run -e DB_URL={host:port} -p 8080:8080 ordermanager
### 取出docker container 裡的檔案例如 log
docker cp <containerID>:/file/path/within/container /host/path/target
