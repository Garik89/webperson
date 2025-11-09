FROM openjdk:17-jdk-slim

# Установка curl для healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Создание директории для приложения
WORKDIR /app

# Копирование JAR файла
COPY target/*.jar app.jar

# Создание директории для config-repo
RUN mkdir -p /config-repo

# Открытие порта
EXPOSE 8888

# Запуск приложения
ENTRYPOINT ["java", "-jar", "/app.jar"]