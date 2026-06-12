# Guia de Ejecucion - Sala Capitular

Esta guia resume como ejecutar y validar los microservicios desde PowerShell con Java 21.

## Requisitos

- XAMPP con MySQL activo en `localhost:3306`.
- Bases de datos creadas segun el README.
- JDK 21 local incluido en `.jdk\jdk-21.0.11+10`.

Activar Java 21 en la terminal actual:

```powershell
cd D:\workspace\Duoc\Sala-Capitular
.\use-java21.ps1
```

Validar:

```powershell
java -version
```

Debe mostrar `openjdk version "21.0.11"`.

## Validar Tests y Cobertura

Desde la raiz:

```powershell
.\build-all.ps1
```

Resultado esperado:

```text
Todos los modulos pasaron clean verify con Java 21.
```

Los tests usan H2, por lo tanto no necesitan XAMPP.

## Ejecutar Microservicios

Para ver el orden y los nombres aceptados:

```powershell
.\run-service.ps1
```

Orden recomendado:

1. Rol (8081)
2. Catalogo (8083)
3. Estado (8084)
4. User (8082)
5. Libro (8085)
6. Estante (8086)
7. Historial (8087)
8. Multas (8088)
9. Detalle (8090)
10. ResenaLibro (8091)
11. ReservaLibro (8089)
12. ApiGateway (8080)

Ejecuta cada servicio en una terminal PowerShell distinta:

```powershell
.\run-service.ps1 Rol
.\run-service.ps1 Catalogo
.\run-service.ps1 Estado
.\run-service.ps1 User
.\run-service.ps1 Libro
```

El script configura `JAVA_HOME` automaticamente antes de arrancar el servicio.

## Swagger

Cada microservicio expone:

```text
http://localhost:{puerto}/swagger-ui/index.html
http://localhost:{puerto}/v3/api-docs
```

Ejemplos:

```text
http://localhost:8081/swagger-ui/index.html
http://localhost:8085/swagger-ui/index.html
http://localhost:8091/swagger-ui/index.html
```

## Problema del IDE

El aviso `Project configuration is not up-to-date with pom.xml` significa que el IDE no recargo Maven despues de modificar los POMs. No bloquea PowerShell.

Soluciones:

- VS Code: `Java: Update Project Configuration`.
- VS Code: `Maven: Reload All Maven Projects`.
- VS Code si persiste: `Java: Clean Java Language Server Workspace`.
- IntelliJ IDEA: `Reload All Maven Projects`.
- Eclipse/STS: `Maven > Update Project`.

## Problemas Frecuentes

### Puerto ocupado

```powershell
netstat -ano | findstr :8081
taskkill /PID <PID> /F
```

### MySQL no conecta

Verifica que XAMPP tenga MySQL iniciado y que exista la base de datos del servicio.

### Maven usa Java incorrecto

Ejecuta:

```powershell
.\use-java21.ps1
.\run-service.ps1 Rol
```
