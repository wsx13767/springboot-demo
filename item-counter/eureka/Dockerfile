
FROM openjdk:11-jre-slim
COPY ./target/*.jar /app/eureka.jar
WORKDIR /app
ENTRYPOINT ["sh", "-c", "java -jar eureka.jar"]