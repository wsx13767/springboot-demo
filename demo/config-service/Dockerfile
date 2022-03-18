FROM openjdk:11-jre-slim
COPY ./target/*.jar /app/config-service.jar
WORKDIR /app
ENTRYPOINT ["sh", "-c", "java -jar config-service.jar"]