FROM openjdk:11-jre-slim
COPY ./target/*.jar /app/server-gateway.jar
WORKDIR /app
ENTRYPOINT ["sh", "-c", "java -jar server-gateway.jar"]
