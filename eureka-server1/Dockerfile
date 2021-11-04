FROM openjdk:11-jre-slim
COPY ./target/*.jar /app/eureka-server.jar
WORKDIR /app
ENTRYPOINT ["sh", "-c", "java -jar demo.jar"]
