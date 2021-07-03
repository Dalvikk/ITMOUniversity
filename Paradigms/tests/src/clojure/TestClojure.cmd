@echo off
if "%~1" == "" (
    echo Usage: %~n0 [test-class] [variant]
    exit /b 1
)

set "REPO=%~dp0.."
set "OUT=__OUT"
set "LIB=%REPO%/clojure/lib/*"

set "CLASS=%~1"
set "ARGS=%~2 %~3"

javac ^
    -d "%OUT%" ^
    "--class-path=%LIB%;%REPO%/clojure;%REPO%/javascript;%REPO%/java" ^
    "%~dp0%CLASS:.=/%.java" ^
 && java -ea "--class-path=%LIB%;%OUT%" "%CLASS%" %ARGS%
