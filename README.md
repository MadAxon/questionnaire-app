# Questionnaire App (Spring Boot + Kotlin + HTML/JS)

Простое full-stack приложение-анкета:
- **Backend:** Spring Boot (Kotlin), Java 17, Gradle.
- **Frontend:** Vanilla HTML5 + CSS3 + JS (пошаговый опрос по 1 вопросу на экран с финальным экраном «Спасибо!»).

---

## Требования

- **Java:** JDK 17 или новее (для локального запуска).
- **Docker & Docker Compose:** для контейнеризированного запуска.

---

## Быстрый запуск

### Вариант 1. Запуск через Docker Compose (Рекомендуемый)
```bash
docker compose up --build
```
или в фоновом режиме:
```bash
docker compose up --build -d
```

Для остановки сервера и контейнера:
```bash
docker compose down
```

### Вариант 2. Через Gradle
```cmd
gradlew.bat bootRun
```

### Вариант 3. Запуск готового JAR
```cmd
gradlew.bat bootJar
java -jar build\libs\questionnaire-app-0.0.1-SNAPSHOT.jar
```

После запуска откройте в браузере:
👉 **http://localhost:8080**

---

## Тестирование и перезапуск

Для прогона тестов с автоматическим перезапуском сервера и обнулением мапы ответов:
```cmd
test_and_restart.bat
```

Для простого прогона тестов:
```cmd
gradlew.bat test
```

Для быстрой очистки памяти и перезапуска сервера без тестов:
```cmd
restart_server.bat
```

---

## API Эндпоинты

- **`GET /questions`** — список предустановленных вопросов (JSON).
- **`POST /answers`** — сохранение ответа пользователя в памяти (тело: `{"questionId": 1, "answer": "...", "sessionId": "..."}`).
- **`GET /answers`** — просмотр всех сохранённых ответов в памяти.
