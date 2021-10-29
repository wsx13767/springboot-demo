FROM openjdk:11-jre-slim
ADD *.c /Users/user/Documents/logs/ordermanager
COPY ./target/*.jar /Documents/mydocker/demo.jar
WORKDIR /Documents/mydocker
ENTRYPOINT ["sh", "-c", "java -jar demo.jar"]
