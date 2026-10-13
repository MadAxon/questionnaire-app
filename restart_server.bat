@echo off
echo ===================================================
echo Остановка сервера на порту 8080...
echo ===================================================
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":8080 " ^| findstr "LISTENING"') do (
    echo Завершение процесса PID %%a...
    taskkill /F /PID %%a >nul 2>&1
)

timeout /t 2 /nobreak >nul 2>&1

echo ===================================================
echo Запуск сервера с чистой мапой ответов...
echo ===================================================
start "QuestionnaireApp Server" cmd /k "java -jar build\libs\questionnaire-app-0.0.1-SNAPSHOT.jar"

echo Сервер перезапущен на http://localhost:8080
