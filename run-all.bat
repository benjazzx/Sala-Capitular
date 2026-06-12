@echo off
REM Ejecutar microservicios en orden de dependencias
REM Fase 1: Servicios sin dependencias

echo ==============================================================
echo        INICIANDO MICROSERVICIOS - FASE 1
echo     Servicios sin dependencias: Rol, Catalogo, Estado
echo ==============================================================
echo.

REM Configurar Java 21
set JAVA_HOME=C:\Users\Felipe\AppData\Local\jdks\jdk-21.0.10
set PATH=%JAVA_HOME%\bin;%PATH%

REM Crear ventanas nuevas para cada servicio
echo [1/3] Iniciando Rol en puerto 8081...
start "Rol - 8081" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\Rol\Rol && .\mvnw.cmd spring-boot:run"

timeout /t 3 /nobreak

echo [2/3] Iniciando Catalogo en puerto 8083...
start "Catalogo - 8083" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\Catalogo\Catalogo && .\mvnw.cmd spring-boot:run"

timeout /t 3 /nobreak

echo [3/3] Iniciando Estado en puerto 8084...
start "Estado - 8084" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\Estado\Estado && .\mvnw.cmd spring-boot:run"

echo.
echo ==============================================================
echo Servicios iniciados. Esperando 10 segundos antes de continuar...
echo ==============================================================
echo.

timeout /t 10 /nobreak

echo.
echo ==============================================================
echo        FASE 2: Servicios con dependencias simples
echo           User (8082) y Libro (8085)
echo ==============================================================
echo.

echo [4/5] Iniciando User en puerto 8082...
start "User - 8082" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\User\User && .\mvnw.cmd spring-boot:run"

timeout /t 3 /nobreak

echo [5/5] Iniciando Libro en puerto 8085...
start "Libro - 8085" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\Libro\Libro && .\mvnw.cmd spring-boot:run"

echo.
echo ==============================================================
echo Servicios iniciados. Esperando 10 segundos antes de continuar...
echo ==============================================================
echo.

timeout /t 10 /nobreak

echo.
echo ==============================================================
echo        FASE 3: Servicios con multiples dependencias
echo   Estante (8086), Historial (8087), Detalle (8090)
echo ==============================================================
echo.

echo [6/8] Iniciando Estante en puerto 8086...
start "Estante - 8086" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\Estante\Estante && .\mvnw.cmd spring-boot:run"

timeout /t 3 /nobreak

echo [7/8] Iniciando Historial en puerto 8087...
start "Historial - 8087" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\Historial\Historial && .\mvnw.cmd spring-boot:run"

timeout /t 3 /nobreak

echo [8/8] Iniciando Detalle en puerto 8090...
start "Detalle - 8090" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\Detalle\Detalle && .\mvnw.cmd spring-boot:run"

echo.
echo ==============================================================
echo Servicios iniciados. Esperando 10 segundos antes de continuar...
echo ==============================================================
echo.

timeout /t 10 /nobreak

echo.
echo ==============================================================
echo        FASE 4: Servicios finales
echo  Multas (8088), ReseñaLibro (8091), ReservaLibro (8089)
echo ==============================================================
echo.

echo [9/11] Iniciando Multas en puerto 8088...
start "Multas - 8088" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\Multas\Multas && .\mvnw.cmd spring-boot:run"

timeout /t 3 /nobreak

echo [10/11] Iniciando ReseñaLibro en puerto 8091...
start "ReseñaLibro - 8091" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\ReseñaLibro\ReseñaLibro && .\mvnw.cmd spring-boot:run"

timeout /t 3 /nobreak

echo [11/11] Iniciando ReservaLibro en puerto 8089...
start "ReservaLibro - 8089" cmd /k "cd d:\workspace\Duoc\Sala-Capitular\ReservaLibro\ReservaLibro && .\mvnw.cmd spring-boot:run"

echo.
echo ==============================================================
echo TODOS LOS SERVICIOS INICIADOS
echo ==============================================================
echo.
echo Documentacion disponible en: docs/
echo Para detener todos los servicios, cierra las ventanas del CMD
echo.
