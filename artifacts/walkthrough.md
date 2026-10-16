# Отчет о реализации: Full-Stack приложение анкеты

## Выполненные задачи

Создано full-stack приложение в директории:
`C:\Users\rusgo\.gemini\antigravity\scratch\questionnaire-app`

### 1. Архитектура и бэкенд (Spring Boot + Kotlin)
- [Models.kt](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/main/kotlin/com/example/questionnaire/model/Models.kt): DTO модели `Question`, `AnswerRequest`, `ApiResponse`.
- [QuestionnaireService.kt](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/main/kotlin/com/example/questionnaire/service/QuestionnaireService.kt):
  - 4 встроенных вопроса для анкеты.
  - Потокобезопасное хранение в памяти на базе `ConcurrentHashMap`.
- [QuestionnaireController.kt](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/main/kotlin/com/example/questionnaire/controller/QuestionnaireController.kt):
  - `GET /questions` — возвращает список вопросов в формате JSON.
  - `POST /answers` — принимает ответ пользователя и сохраняет его в памяти.
  - `GET /answers` — просмотр всех накопленных ответов в памяти.
  - Включен `@CrossOrigin(origins = ["*"])` для гибкости вызова.

### 2. Фронтенд (HTML5 + CSS3 + Vanilla JS)
- [index.html](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/main/resources/static/index.html):
  - Пошаговое прохождение анкеты (1 вопрос на экране).
  - Индикатор прогресса и процент выполнения («Вопрос 1 из 4»).
  - Валидация ввода (не позволяет отправить пустой ответ).
  - Отправка каждого ответа на `POST /answers` по сети.
  - По завершении всех вопросов — отображение поздравительного экрана **«Спасибо!»** с кнопкой повторного прохождения.

---

## Верификация и результаты тестирования

### 1. Автоматические тесты и методика перезапуска
- Создан командный скрипт [`test_and_restart.bat`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/test_and_restart.bat):
  1. Выполняет запуск тестов: `gradlew.bat test`.
  2. При успешном завершении находит и останавливает предыдущий процесс приложения на порту `8080`.
  3. Пересобирает и запускает чистый экземпляр сервера `java -jar build\libs\questionnaire-app-0.0.1-SNAPSHOT.jar`.
  4. В результате внутренняя мапа ответов в памяти полностью очищается (`{}`).
- Создан отдельный скрипт [`restart_server.bat`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/restart_server.bat) для быстрой очистки памяти и перезапуска сервера без повторного прогона тестов.

---

## Инструкция по запуску

- **GET http://localhost:8080/questions:**
  ```json
  [
    {"id":1,"text":"Какой ваш любимый язык программирования?"},
    {"id":2,"text":"Какой фреймворк вы предпочитаете для бэкенда?"},
    {"id":3,"text":"Сколько лет вы занимаетесь разработкой?"},
    {"id":4,"text":"Какой формат работы вы предпочитаете (удалёнка, офис или гибрид)?"}
  ]
  ```

- **POST http://localhost:8080/answers:**
  Отправка: `{"questionId": 1, "answer": "Kotlin", "sessionId": "user_42"}`
  Ответ:
  ```json
  {"status":"success","message":"Answer saved successfully"}
  ```

- **GET http://localhost:8080/answers:**
  Подтверждение сохранения в памяти:
  ```json
  {"user_42":{"1":"Kotlin"}}
  ```

- **GET http://localhost:8080/**:
  Возвращает `HTTP 200 OK` и отдает веб-интерфейс (`text/html`).

---

## Инструкция по запуску

Приложение сейчас запущено на порту `8080`.

Вы можете открыть интерфейс в браузере:
👉 **[http://localhost:8080](http://localhost:8080)**

Для последующего самостоятельного запуска из терминала:
```cmd
cd C:\Users\rusgo\.gemini\antigravity\scratch\questionnaire-app
gradlew.bat bootRun
```
или запуск готового JAR:
```cmd
java -jar build\libs\questionnaire-app-0.0.1-SNAPSHOT.jar
```
