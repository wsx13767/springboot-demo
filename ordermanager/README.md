## MAVEN
* mvn clean install
## DOCKER
### image
* cd ./ordermanager-appliacation
* docker build -t ordermanager .
### container -e 指定變數，application.properties裡使用${}，如${DB_URL}
* docker run -e DB_URL={host:port} -p 8080:8080 ordermanager
### 取出docker container 裡的檔案例如 log
docker cp `<containerID>`:/file/path/within/container /host/path/target



## 將整個project放入docker，在docker裡compile java

```dockerfile
#
# build stage
#
FROM maven:3.6.0-jdk-11-slim AS builder
COPY . /ordermanager
RUN mvn -f /ordermanager/pom.xml clean install

#
# package stage
#
FROM openjdk:11-jre-slim
ADD *.c /Users/user/Documents/logs/ordermanager
COPY --from=builder /ordermanager/ordermanager-application/target/*.jar  /Documents/mydocker/demo.jar
WORKDIR /Documents/mydocker
ENTRYPOINT ["sh", "-c", "java -jar demo.jar"]
```

