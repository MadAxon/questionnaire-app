# Все использованные промпты:
### Схема:
>Мой промпт
>>Ответ агента (или троеточие, если ответ не представляет важности, например ответы на пуш в git)

### Промпты:
>[/plan](slashCommand;plan) Нужно создать простое full-stack приложение. 
Backend на spring boot, язык kotlin, два эндпоинта:
GET /questions — список из 3–5 жёстко заданных вопросов;
POST /answers — приём ответов и хранение в памяти (map)
Frontend на HTML+JS: загружает все вопросы и отображает по порядку, 1 вопрос на экране, после ответа пользователем на вопрос - следующий вопрос. После окончании вопросов отображаем страницу с надписью "Спасибо!".
>> Я подготовил подробный план реализации full-stack приложения: [implementation_plan.md](file:///C:/Users/rusgo/.gemini/antigravity/brain/05bd4881-41d9-465e-a058-bf7c75ca38d8/implementation_plan.md).
>> ### Краткое резюме:
>>1. **Проект:** Spring Boot 3 на Kotlin с Gradle Wrapper в директории `C:\Users\rusgo\.gemini\antigravity\scratch\questionnaire-app`.
>>2. **Backend:**
>>   - `GET /questions`: возвращает список из 4 вопросов.
>>   - `POST /answers`: принимает ответ и сохраняет в потокобезопасной `ConcurrentHashMap` в памяти.
>>3. **Frontend:**
>>   - HTML + CSS + JS в `src/main/resources/static/index.html` (доступен на `http://localhost:8080/`).
>>   - Пошаговый показ вопросов (1 вопрос на экран), отправка ответа на бэкенд, переход к следующему и финальный экран **«Спасибо!»**.
>>4. **Тестирование:**
>>   - Автоматические тесты контроллеров и сервиса (`./gradlew test`).
>>   - Запуск приложения и проверка в браузере.
>>Пожалуйста, подтвердите план или напишите, если хотите внести изменения, и я приступлю к реализации!
---
>Подтверждаю план
>>Full-stack приложение успешно создано, протестировано и запущено!
>>Подробный отчет доступен в артефакте: [walkthrough.md](file:///C:/Users/rusgo/.gemini/antigravity/brain/05bd4881-41d9-465e-a058-bf7c75ca38d8/walkthrough.md).
>>### Где находятся файлы проекта
>>Каталог проекта: `C:\Users\rusgo\.gemini\antigravity\scratch\questionnaire-app` *(рекомендуется сделать его активным воркспейсом при необходимости)*.
>>- Модели данных: [Models.kt](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/main/kotlin/com/example/questionnaire/model/Models.kt)
>>- Сервис хранения в памяти: [QuestionnaireService.kt](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/main/kotlin/com/example/questionnaire/service/QuestionnaireService.kt)
>>- Контроллер API: [QuestionnaireController.kt](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/main/kotlin/com/example/questionnaire/controller/QuestionnaireController.kt)
>>- Веб-интерфейс: [index.html](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/main/resources/static/index.html)
>>- Автотесты: [QuestionnaireControllerTest.kt](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/test/kotlin/com/example/questionnaire/QuestionnaireControllerTest.kt)
>>---
>>### Проверка работы
>>Сервер в данный момент запущен в фоне. Вы можете открыть браузер и сразу пройти опрос:
👉 **http://localhost:8080**
>>- Вопросы отображаются по одному на экран с прогресс-баром.
>>- После каждого ответа данные отправляются на `POST /answers`.
>>- По окончании вопросов открывается финальный экран **«Спасибо!»**.
---
>В json ответе от GET /answers есть лишние данные, добавленные от тестов. Определи какие это данные и не добавляй их в итоговую мапу. Тест при этом не удалять, он должен проверять работу эндпоинта
>>### 1. Какие данные были определены как тестовые
>>В ответе `GET /answers` присутствовали:
>>- **`user_42`** — идентификатор сессии с ответом `{"1": "Kotlin"}`, созданный в ходе ручной проверки эндпоинта через curl на предыдущем шаге.
>>- **`test_session_123`** (и любые сессии с префиксом `test` или словом `test`) — идентификаторы тестовых сессий автотестов.
>>---
>>### 2. Что было сделано
>>1. **Исключение тестовых данных из мапы:**
>>   В [`QuestionnaireService.kt`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/main/kotlin/com/example/questionnaire/service/QuestionnaireService.kt) добавлена проверка `isTestSession`:
>>   - Если `sessionId` относится к тестовым (`user_42`, начинается с `test` или содержит `test`), метод `saveAnswer` **не добавляет** запись в память `answersStore`.
>>   - Метод `getAllAnswers()` дополнительно фильтрует ключи перед возвратом результата клиенту.
>>2. **Сохранение и обновление тестов:**
>>   Тестовый класс [`QuestionnaireControllerTest.kt`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/src/test/kotlin/com/example/questionnaire/QuestionnaireControllerTest.kt) сохранён и теперь проверяет оба сценария:
>>   - Проверяет сохранение ответа для реального пользователя (`sess_user_1001`) в итоговой мапе (`GET /answers`).
>>   - Проверяет, что при отправке тестовых данных (`test_session_123`) эндпоинт `POST /answers` завершается успешно (статус `200 OK`), но эти данные **не попадают** в итоговую мапу (`jsonPath("$.test_session_123") { doesNotExist() }`).
>>---
>>### 3. Результаты проверки
>>- **Автотесты:** все 3 теста успешно пройдены:
>>  - `should return list of questions on GET questions()` — **PASSED**
>>  - `should save answer on POST answers for user session()` — **PASSED**
>>  - `should not add test data to final answers map on POST answers()` — **PASSED**
>>- **Проверка на запущенном сервере (`http://localhost:8080`):**
>>  - При отправке ответов от `user_42` и `test_session_123` возвращается `200 OK`.
>>  - При отправке ответа от `sess_real_user_abc` данные сохраняются.
>>  - Запрос `GET /answers` возвращает только реальные пользовательские данные:
>>    ```json
>>    {
>>      "sess_real_user_abc": {
>>        "1": "Kotlin & Spring"
>>      }
>>    }
>>    ```
>>    Лишние тестовые сессии в ответе отсутствуют.
---
>Отменяем новые изменения, верни как было.
>>...
---
>Измени методику запуска тестов: после прогона тестов перезапустить сервер, чтобы мапа обнулилась.
>>Методика запуска тестов обновлена.
>>### Что сделано:
>>1. Создан скрипт **[`test_and_restart.bat`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/test_and_restart.bat)**:
>>   - Запускает тесты через `gradlew.bat test`.
>>   - Если тесты завершились успешно, автоматически находит процесс на порту `8080` и завершает его (`taskkill`).
>>   - Пересобирает JAR-файл и запускает чистый экземпляр сервера.
>>   - **Результат:** сервер перезапускается с абсолютно чистой мапой в памяти (`{}`).
>>2. Создан скрипт **[`restart_server.bat`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/restart_server.bat)**:
>>   - Служит для быстрого перезапуска сервера и очистки мапы ответов без повторного прогона тестов.
>>---
>>### Как теперь запускать тесты:
>>Из командной строки (cmd) в папке проекта:
>>```cmd
>>cd C:\Users\rusgo\.gemini\antigravity\scratch\questionnaire-app
>>test_and_restart.bat
>>```
>>Сервер в данный момент перезапущен и работает с чистой мапой:
>>- Проверка ответов: `GET http://localhost:8080/answers` возвращает `{}`
>>- Веб-интерфейс доступен: **http://localhost:8080**
---
>Запушим проект в github, имя пользователя MadAxon
>>...
---
>Репозиторий с именем questionnaire-app создал. Git Credential Manager не открыл окно в браузере или окне
>>...
---
>Сформируй короткую инструкцию по запуску и запиши в файл README
>>...
---
>Добавь docker в проект 
>Dockerfile docker-compose.yml и чтобы проект запускался одной командой "docker compose up --build"
>>В проект добавлена поддержка Docker и Docker Compose. Все изменения закоммичены и отправлены в ваш GitHub-репозиторий.
>>### Что было создано и настроено:
>>1. **[`Dockerfile`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/Dockerfile):**
>>   - **Многоэтапная сборка (Multi-stage build):**
>>     - **Этап сборки (`builder`):** образ `eclipse-temurin:17-jdk-jammy`, копирование Gradle Wrapper и исходного кода, сборка оптимизированного JAR (`./gradlew bootJar -x test`). Автоматически исправлены окончания строк Windows (`CRLF` -> `LF`) для запуска `gradlew` в Linux.
>>     - **Финальный образ:** минимальный runtime `eclipse-temurin:17-jre-jammy`.
>>   - **Безопасность:** запуск от непривилегированного пользователя `spring:spring`.
>>   - **Порт:** `8080`.
>>2. **[`docker-compose.yml`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/docker-compose.yml):**
>>   - Конфигурация сервиса `app` с пробросом порта `8080:8080` и политикой перезапуска.
>>3. **[`.dockerignore`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/.dockerignore):**
>>   - Исключает директории `.git`, `.gradle`, `build` и временные файлы из контекста сборки Docker, обеспечивая быструю сборку.
>>4. **[`README.md`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/README.md):**
>>   - Добавлена инструкция по запуску через Docker Compose.
>>---
>>### Запуск проекта одной командой:
>>В корне проекта выполните:
>>```bash
>>docker compose up --build
>>```
>>или в фоновом режиме:
>>```bash
>>docker compose up --build -d
>>```
>>После запуска приложение сразу доступно в браузере:
👉 **http://localhost:8080**
---
>Добавь директорию screenshots в .dockerignore
>>...
---
>Добавь в Docker и проверку, что контейнер отвечает на GET /questions
>>В конфигурацию Docker и Docker Compose добавлена проверка работоспособности (**Healthcheck**) эндпоинта `GET /questions`. Изменения закоммичены и отправлены в GitHub-репозиторий.
>>---
>>### Что было добавлено:
>>1. **В [`Dockerfile`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/Dockerfile):**
>>   - Установлен легковесный утилитарный пакет `curl` в runtime-образ.
>>   - Добавлена инструкция `HEALTHCHECK`:
>>     ```dockerfile
>>     HEALTHCHECK --interval=10s --timeout=5s --start-period=20s --retries=3 \
>>       CMD curl -f http://localhost:8080/questions || exit 1
>>     ```
>>2. **В [`docker-compose.yml`](file:///C:/Users/rusgo/.gemini/antigravity/scratch/questionnaire-app/docker-compose.yml):**
>>   - Добавлен блок `healthcheck` для сервиса `app`:
>>     ```yaml
>>     healthcheck:
>>       test: ["CMD-SHELL", "curl -f http://localhost:8080/questions || exit 1"]
>>       interval: 10s
>>       timeout: 5s
>>       retries: 3
>>       start_period: 20s
>>     ```
>>---
>>### Как это работает:
>>- **`start_period: 20s`** — даёт приложению Spring Boot 20 секунд на запуск до того, как проверки начнут влиять на статус.
>>- **`curl -f http://localhost:8080/questions`** — запрашивает эндпоинт. Флаг `-f` возвращает код ошибки, если HTTP-статус $\ge 400$.
>>- В выводе команды `docker ps` или `docker compose ps` статус контейнера автоматически изменится с `(health: starting)` на **`(healthy)`**.
---
>Добавь в README информацию про docker compose down для сервера проекта
>>...
---
>Добавь в REEADME команду проверки curl http://localhost:8080/questions
>>...
---
>Добавь в README ссылки на оба скриншота из директории srcreenshots. Ссылки должны ссылаться на github репозиторий, а не на локальный path
>>...
---

### Что модель не поняла:
Модель с первого раза собрала рабочий проект, однако меня смутило наличие данных с прогона тестов в мапе. Решение, выданное моделью мне не понравилось, поэтому в конечном счёте попросил перезапускать сервер после прогона тестов, чтобы мапа очищалась от данных, это сработало.

### Выбранный AI-setup:
Antigravity, модель - gemini flash 3.8 medium, плагины дефолтные, какие-либо кастомные настройки не использовались

### Рассмотренные альтернативы и мой выбор:
* Cursor с моделью grok 4.6 medium - выполнил задачу также с первого промта, но в отличии от gemini flash 3.8 не сохранял данные всех ответов (сбрасывал мапу при обновлении html страницы), дальше поэкспериментировать не получилось - закончились токены.
* Claude - Запустить не получилось. При регистрации выдаёт ошибку: _К сожалению, новые пользователи сейчас не могут пользоваться Claude. Мы усердно работаем над тем, чтобы в ближайшее время расширить доступ._
* Codex - Регистрация не возможна. Требует зарубежный номер телефона.
* opencode + ollama + локальный qwen2.5-coder:7b-16k - любые решения выводит в консоль в виде чата, получается своего рода инструкция что и как создавать. Сам промпт, каковой был отправлен в antigravity в моде plan описывает корректно, но в коде содержатся ошибки. Модели нужно более точное описание что делать. Прямой промпт создать любой файл на диске не создаёт его, а требует действия от меня. Плагин continue в этом случае решает проблему. Вывод от модели плагин обрабатывает и отображает в интерфейсе ide стандартные apply, undo и т.д.
* antigravity - мой выбор. Причины
    1) для активации подписки на современную модель gemini не нужен зарубежный номер, не нужна зарубежная карта (покупка ключа на маркетплейсах), не нужен vpn, не нужно создавать отдельный аккаунт, не нужно обращаться в какие-то сомнительные комьюнити
    2) для работы ide не нужен vpn (обход региональных ограничений посредством анлокера, решение доступно на github)
    3) gemini + antigravity = максимальная совместимость

### Впечатления и мысли:
Antigravity "заточен" только под узкий список моделей, что не позволяет экспериментировать с другими, как в Cursor (доступны все модели от openai, grok, claude) или opencode (любая локальная модель + open router). Но в целом при использовании моделей семейства gemini и объёме 1М токенов такой setup более чем достаточен для моих задач. По задаче: инструмент сходу сформировал рабочий вариант, без доп. промптов сформировал прогон тестов, соблюдает некие принципы дизайна относительно ui фронта, применяет стили. Понравилось необходимость подтверждения пользователем каждого запуска cmd команды. Автоматически подтягивает необходимые skills для контескной работы, например для работы html подтянул generative_ui, попросил запушить в гит - permissioned-github. Сама ide - форк от vscode, поэтому все плагины поддерживаются.
При первичном запуске тестов они свалились с ошибкой компиляции. В логах thinking можно было увидеть такой пункт
>The unresolved references suggest a missing Spring Boot testing dependency. Suspect the project might be missing a necessary test-specific library or annotation. Reviewing the dependency management section for the inclusion of the Spring Boot test starter is the next logical step.

На что модель сразу же изменила структура конфига и изменила фраемворки проекта, например ```org.springframework.boot:spring-boot-starter-webmvc``` на ```org.springframework.boot:spring-boot-starter-web```, ```tools.jackson.module:jackson-module-kotlin``` на ```com.fasterxml.jackson.module:jackson-module-kotlin``` и т.д. после чего билд тестов и их выполнение было успешным

### Возникшие проблемы:
Модель по умолчанию выполняла все консольные команды в powershell. Из-за специфичных настроек моей ОС любая подобная операция сваливалась с SecurityException, пришлось явно просить использовать cmd. Причем даже после использования cmd ошибки продолжали сыпаться, несмотря на успешное выполнение этих команд, что влекло захломление мусорными логами размышления модели.
Модель сама решила использовать не ```map<string, string>``` в качестве хранения данных, а ```map<string, map<string, string>>``` руководствуясь тем, что в родительский ключ будет записываться сессия пользователя, то есть модель "додумала" такой функционал, хотя её не просили об этом. В задаче не описывалась структура мапы, поэтому оставил как есть. 

### Вывод:
Данный setup с лёгкостью справился с простым заданием + ввиду имеющейся pro подписки, получаемой самой что ни на есть простым способом и возможностью работать в IDE без использования vpn, останавливаюсь на этом выборе. Плюс ко всему из личного пратического опыта мне привычней работать с продуктами google, нежели с другими. Если по каким-то причинам потребуется взаимодействие с какой-то конкретной моделью, условно codex, то придётся отказаться от выбранного ide в пользу другого, в этом есть своеобразный риск. Однако у antigravity есть неоспоримое преимущество: это доступные 1М+ токенов.