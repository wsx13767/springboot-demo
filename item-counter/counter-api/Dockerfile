FROM openjdk:11-jre-slim
COPY ./target/*.jar /app/counter-api.jar
WORKDIR /app
ENTRYPOINT ["sh", "-c", "java -jar counter-api.jar"]