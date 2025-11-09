FROM openjdk:17-slim
WORKDIR /app
COPY target/api-gateway-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]