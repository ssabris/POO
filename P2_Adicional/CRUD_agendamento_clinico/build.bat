@echo off
REM ============================================================
REM  build.bat  –  Compila e executa o Sistema Clinico (Windows)
REM  Requer: Java 21+ e Maven 3.6+ instalados e no PATH
REM ============================================================

echo ==== Compilando com Maven ====
mvn package -q
if errorlevel 1 (
    echo Erro na compilacao!
    pause
    exit /b 1
)

echo.
echo ==== Executando ====
java -jar target\clinica.jar
pause
