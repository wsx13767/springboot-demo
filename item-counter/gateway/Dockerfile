FROM openjdk:11-jre-slim
COPY ./target/*.jar /app/gateway.jar
WORKDIR /app
ENTRYPOINT ["sh", "-c", "java -jar gateway.jar"]