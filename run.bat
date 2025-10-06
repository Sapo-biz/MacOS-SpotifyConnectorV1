@echo off
REM Spotify Connector Run Script for Windows
REM This script compiles and runs the Spotify Connector application

echo Starting Spotify Connector...

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo Java version:
java -version
echo.
echo Maven version:
mvn -version
echo.

REM Check if configuration exists
if not exist "src\main\resources\spotify.properties" (
    echo Configuration file not found. Creating from template...
    copy "src\main\resources\spotify-default.properties" "src\main\resources\spotify.properties"
    echo Please edit src\main\resources\spotify.properties with your Spotify credentials
    echo Then run this script again.
    pause
    exit /b 1
)

REM Compile the application
echo Compiling application...
mvn clean compile

if %errorlevel% neq 0 (
    echo Compilation failed. Please check the error messages above.
    pause
    exit /b 1
)

echo Compilation successful. Starting application...

REM Run the application
mvn javafx:run

echo Application finished.
pause
