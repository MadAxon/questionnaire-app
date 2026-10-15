# Этап 1: Сборка приложения
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

# Копируем Gradle Wrapper и конфигурации сборки
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle

# Исправляем окончания строк Windows (CRLF -> LF) и даем права на исполнение
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Загружаем зависимости (слой кэшируется, если зависимости не менялись)
RUN ./gradlew dependencies --no-daemon || true

# Копируем исходный код
COPY src ./src

# Собираем JAR-пакет без повторного запуска тестов внутри Docker-сборки
RUN ./gradlew bootJar -x test --no-daemon

# Этап 2: Финальный легковесный образ с JRE
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Создаем непривилегированного пользователя для безопасности
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Копируем собранный JAR-архив из этапа сборки
COPY --from=builder /app/build/libs/questionnaire-app-0.0.1-SNAPSHOT.jar app.jar

# Порт приложения
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
