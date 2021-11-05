FROM openjdk:11-jre-slim
COPY ./target/*.jar /app/server-admin.jar
WORKDIR /app
ENTRYPOINT ["sh", "-c", "java -jar server-admin.jar"]
