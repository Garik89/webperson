FROM openjdk:17-slim
WORKDIR /app
COPY target/notification-service-1.0.0.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]