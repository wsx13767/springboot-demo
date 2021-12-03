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