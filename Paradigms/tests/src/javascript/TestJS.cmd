@echo off

if "%~2" == "" (
    echo Usage: %~n0 [test-class] [variant]
    exit /b 1
)

set "JS=%~dp0"
set "JAVA=%JS%../java"
set "OUT=__OUT"
set "CLASS=%~1"
set "VARIANT=%~2"

if not exist "%OUT%" mkdir "%OUT%"

javac -d "%OUT%" "--class-path=%JS%;%JAVA%" "%JS%%CLASS:.=\%.java" ^
  && java -ea "--module-path=%JS%graal" "--class-path=%OUT%" "%CLASS%" "%VARIANT%"
