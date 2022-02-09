FROM openjdk:11-jre-slim
COPY ./target/*.jar /app/batch.jar
WORKDIR /app
ENTRYPOINT ["sh", "-c", "java -jar batch.jar"]