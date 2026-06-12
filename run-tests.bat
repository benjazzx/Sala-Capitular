@echo off
REM Script para ejecutar tests y generar reporte de cobertura JaCoCo

echo ==============================================================
echo        EJECUTANDO TESTS Y COBERTURA CON JACOCO
echo           Sala Capitular - Java 21
echo ==============================================================
echo.

setlocal enabledelayedexpansion

set JAVA_HOME=C:\Users\Felipe\AppData\Local\jdks\jdk-21.0.10
set PATH=%JAVA_HOME%\bin;%PATH%

REM Servicios a probar
set SERVICES=Rol User Catalogo Estado Libro Estante Historial Multas ReservaLibro Detalle

set SUCCESS_COUNT=0
set FAILURE_COUNT=0

for %%S in (%SERVICES%) do (
    echo.
    echo [%%S] Ejecutando tests y generando cobertura...
    
    cd "d:\workspace\Duoc\Sala-Capitular\%%S\%%S"
    
    if exist mvnw.cmd (
        REM Ejecutar tests con cobertura
        call mvnw.cmd clean test jacoco:report >nul 2>&1
        
        if !errorlevel! equ 0 (
            echo   [OK] Tests ejecutados - Cobertura generada
            set /A SUCCESS_COUNT=!SUCCESS_COUNT!+1
            
            REM Verificar si el reporte existe
            if exist "target\site\jacoco\index.html" (
                echo   Reporte disponible en: target\site\jacoco\index.html
            )
        ) else (
            echo   [ERROR] Tests o cobertura fallaron
            set /A FAILURE_COUNT=!FAILURE_COUNT!+1
        )
    ) else (
        echo   [NO ENCONTRADO] mvnw.cmd no existe
    )
)

echo.
echo ==============================================================
echo         RESUMEN DE TESTS Y COBERTURA
echo ==============================================================
echo.
echo Tests ejecutados: %SUCCESS_COUNT%
echo Servicios con errores: %FAILURE_COUNT%
echo.
echo Reportes de cobertura disponibles en:
echo   Rol\Rol\target\site\jacoco\index.html
echo   User\User\target\site\jacoco\index.html
echo   Catalogo\Catalogo\target\site\jacoco\index.html
echo   Estado\Estado\target\site\jacoco\index.html
echo   Libro\Libro\target\site\jacoco\index.html
echo   Estante\Estante\target\site\jacoco\index.html
echo   Historial\Historial\target\site\jacoco\index.html
echo   Multas\Multas\target\site\jacoco\index.html
echo   ReservaLibro\ReservaLibro\target\site\jacoco\index.html
echo   Detalle\Detalle\target\site\jacoco\index.html
echo.
echo ==============================================================
echo.

cd d:\workspace\Duoc\Sala-Capitular
