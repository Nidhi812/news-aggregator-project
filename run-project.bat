@echo off
echo ========================================
echo News Aggregator Project
echo ========================================
echo.
echo Starting the project with Docker...
echo.

REM Check if Docker is running
docker version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not running or not installed!
    echo Please start Docker Desktop and try again.
    pause
    exit /b 1
)

echo Building and starting all services...
docker-compose up --build

echo.
echo ========================================
echo Project is running!
echo ========================================
echo Frontend: http://localhost:3000
echo Backend API: http://localhost:8080
echo Admin Dashboard: http://localhost:3000/admin
echo.
echo Default Admin Credentials:
echo Email: admin@newsaggregator.com
echo Password: admin123
echo.
pause 