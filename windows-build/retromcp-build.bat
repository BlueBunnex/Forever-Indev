@echo off

REM Get the directory one level up from the current script
set SCRIPT_DIR=%~dp0..
set JAVA_EXECUTABLE=%SCRIPT_DIR%\windows-build\jre-1.8\bin\java.exe
set JAR_FILE=%SCRIPT_DIR%\..\RetroMCP-Java-Maxxx-all.jar

REM Check if the Java executable exists
if not exist "%JAVA_EXECUTABLE%" (
  echo [ERROR]: Java executable not found at %JAVA_EXECUTABLE%
  pause
  exit /b 1
)

REM Check if the JAR file exists
if not exist "%JAR_FILE%" (
  echo [ERROR]: JAR file not found at %JAR_FILE%
  pause
  exit /b 1
)

REM Change to the directory one level above the script directory
cd %SCRIPT_DIR%\..

REM Run the Java command with the 'build' argument
echo Running build command: %JAVA_EXECUTABLE% -jar %JAR_FILE% build
%JAVA_EXECUTABLE% -jar %JAR_FILE% build

REM Capture the exit status of the command
set EXIT_STATUS=%ERRORLEVEL%

REM Check if the command was successful
if %EXIT_STATUS% neq 0 (
  echo [ERROR]: Build finished with errors! Exit status: %EXIT_STATUS%
) else (
  echo Build 100% [==========] Finished!
)

REM Keep the terminal window open after execution
pause
