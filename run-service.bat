@echo off
REM Ejecutar un servicio específico
REM Uso: run-service.bat nombre_servicio

set SERVICE=%1

if "%SERVICE%"=="" (
    echo Uso: run-service.bat nombre_servicio
    echo.
    echo Servicios disponibles:
    echo   Rol         (puerto 8081)
    echo   User        (puerto 8082)
    echo   Catalogo    (puerto 8083)
    echo   Estado      (puerto 8084)
    echo   Libro       (puerto 8085)
    echo   Estante     (puerto 8086)
    echo   Historial   (puerto 8087)
    echo   Multas      (puerto 8088)
    echo   ReservaLibro (puerto 8089)
    echo   Detalle     (puerto 8090)
    echo   ReseñaLibro  (puerto 8091)
    echo.
    echo Ejemplo: run-service.bat Rol
    exit /b 1
)

REM Configurar Java 21
set JAVA_HOME=C:\Users\Felipe\AppData\Local\jdks\jdk-21.0.10
set PATH=%JAVA_HOME%\bin;%PATH%

REM Determinar puerto y ruta
if /i "%SERVICE%"=="Rol" (
    set PORT=8081
    set PATH_SERVICE=Rol\Rol
) else if /i "%SERVICE%"=="User" (
    set PORT=8082
    set PATH_SERVICE=User\User
) else if /i "%SERVICE%"=="Catalogo" (
    set PORT=8083
    set PATH_SERVICE=Catalogo\Catalogo
) else if /i "%SERVICE%"=="Estado" (
    set PORT=8084
    set PATH_SERVICE=Estado\Estado
) else if /i "%SERVICE%"=="Libro" (
    set PORT=8085
    set PATH_SERVICE=Libro\Libro
) else if /i "%SERVICE%"=="Estante" (
    set PORT=8086
    set PATH_SERVICE=Estante\Estante
) else if /i "%SERVICE%"=="Historial" (
    set PORT=8087
    set PATH_SERVICE=Historial\Historial
) else if /i "%SERVICE%"=="Multas" (
    set PORT=8088
    set PATH_SERVICE=Multas\Multas
) else if /i "%SERVICE%"=="ReservaLibro" (
    set PORT=8089
    set PATH_SERVICE=ReservaLibro\ReservaLibro
) else if /i "%SERVICE%"=="Detalle" (
    set PORT=8090
    set PATH_SERVICE=Detalle\Detalle
) else if /i "%SERVICE%"=="ReseñaLibro" (
    set PORT=8091
    set PATH_SERVICE=ReseñaLibro\ReseñaLibro
) else (
    echo Error: Servicio "%SERVICE%" no reconocido
    exit /b 1
)

echo ==============================================================
echo   Iniciando %SERVICE% en puerto %PORT%
echo ==============================================================
echo.
echo Swagger disponible en: http://localhost:%PORT%/swagger-ui.html
echo.

cd "d:\workspace\Duoc\Sala-Capitular\%PATH_SERVICE%"
call mvnw.cmd spring-boot:run
