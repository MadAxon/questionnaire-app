@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo [1/3] Запуск автоматических тестов...
echo ===================================================
call gradlew.bat test
if %ERRORLEVEL% neq 0 (
    echo.
    echo [ОШИБКА] Тесты завершились с ошибкой! Сервер не будет перезапущен.
    exit /b %ERRORLEVEL%
)

echo.
echo ===================================================
echo [2/3] Тесты пройдены! Остановка сервера на порту 8080...
echo ===================================================
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":8080 " ^| findstr "LISTENING"') do (
    echo Остановка процесса PID %%a...
    taskkill /F /PID %%a >nul 2>&1
)

timeout /t 2 /nobreak >nul 2>&1

echo.
echo ===================================================
echo [3/3] Запуск чистого сервера...
echo ===================================================
call gradlew.bat bootJar
start "QuestionnaireApp Server" cmd /k "java -jar build\libs\questionnaire-app-0.0.1-SNAPSHOT.jar"

echo.
echo ===================================================
echo [ГОТОВО] Сервер перезапущен!
echo Внутренняя мапа ответов в памяти обнулена.
echo Приложение доступно: http://localhost:8080
echo ===================================================
