## MAVEN
* mvn clean install
## DOCKER
* cd ./ordermanager-appliacation
* docker build -t ordermanager .
* docker run -p 8080:8080 ordermanager
### 取出docker container 裡的檔案例如 log
docker cp <containerID>:/file/path/within/container /host/path/target
