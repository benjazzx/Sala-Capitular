@echo off
REM Script para compilar y diagnosticar todos los microservicios

echo ==============================================================
echo        DIAGNOSTICO Y COMPILACION DE MICROSERVICIOS
echo          Sala Capitular - Java 21
echo ==============================================================
echo.

setlocal enabledelayedexpansion

set JAVA_HOME=C:\Users\Felipe\AppData\Local\jdks\jdk-21.0.10
set PATH=%JAVA_HOME%\bin;%PATH%

REM Verificar Java
echo Verificando Java...
java -version
echo.

REM Compilar cada servicio
echo ==============================================================
echo         COMPILACION POR SERVICIO
echo ==============================================================
echo.

set SUCCESS_COUNT=0
set FAILURE_COUNT=0

for %%S in (Rol User Catalogo Estado Libro Estante Historial Multas ReservaLibro Detalle ReseñaLibro) do (
    echo [%%S] Compilando...
    
    if "%%S"=="ReseñaLibro" (
        cd "d:\workspace\Duoc\Sala-Capitular\ReseñaLibro\ReseñaLibro"
    ) else (
        cd "d:\workspace\Duoc\Sala-Capitular\%%S\%%S"
    )
    
    if exist mvnw.cmd (
        call mvnw.cmd clean compile -DskipTests >nul 2>&1
        if !errorlevel! equ 0 (
            echo   [OK] %%S compilado exitosamente
            set /A SUCCESS_COUNT=!SUCCESS_COUNT!+1
        ) else (
            echo   [ERROR] %%S fallo la compilacion
            set /A FAILURE_COUNT=!FAILURE_COUNT!+1
        )
    ) else (
        echo   [NO ENCONTRADO] %%S
    )
)

echo.
echo ==============================================================
echo         RESUMEN
echo ==============================================================
echo.
echo Compilados exitosamente: %SUCCESS_COUNT%
echo Errores: %FAILURE_COUNT%
echo.
echo ==============================================================
echo         PUERTOS DISPONIBLES
echo ==============================================================
echo.
echo Rol (8081)         - http://localhost:8081/swagger-ui.html
echo User (8082)        - http://localhost:8082/swagger-ui.html
echo Catalogo (8083)    - http://localhost:8083/swagger-ui.html
echo Estado (8084)      - http://localhost:8084/swagger-ui.html
echo Libro (8085)       - http://localhost:8085/swagger-ui.html
echo Estante (8086)     - http://localhost:8086/swagger-ui.html
echo Historial (8087)   - http://localhost:8087/swagger-ui.html
echo Multas (8088)      - http://localhost:8088/swagger-ui.html
echo ReservaLibro (8089) - http://localhost:8089/swagger-ui.html
echo Detalle (8090)     - http://localhost:8090/swagger-ui.html
echo ReseñaLibro (8091) - http://localhost:8091/swagger-ui.html
echo.
echo ==============================================================
echo.
cd d:\workspace\Duoc\Sala-Capitular
