# Resumen de Resolucion - Sala Capitular

Fecha: 2026-06-12  
Estado: Completado

## Java 21

El proyecto quedo configurado para Java 21 en los 11 microservicios y en `api-gateway`.

JDK validado localmente:

```text
D:\workspace\Duoc\Sala-Capitular\.jdk\jdk-21.0.11+10
openjdk version "21.0.11" 2026-04-21 LTS
```

Para activar Java 21 en una terminal PowerShell desde la raiz del proyecto:

```powershell
.\use-java21.ps1
```

## Validacion de Tests

Comando ejecutado:

```powershell
.\build-all.ps1
```

Resultado:

```text
Todos los modulos pasaron clean verify con Java 21.
```

La validacion incluye:

- Rol
- Catalogo
- Estado
- User
- Libro
- Estante
- Historial
- Multas
- Detalle
- ResenaLibro
- ReservaLibro
- ApiGateway

Los tests usan H2 con perfil `test`, por lo que no requieren XAMPP.

## Ejecucion Local con XAMPP

Para correr los servicios reales, XAMPP/MySQL debe estar activo en `localhost:3306` y las bases deben existir.

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

Ejecutar un servicio desde la raiz:

```powershell
.\run-service.ps1 Rol
```

Ver servicios disponibles:

```powershell
.\run-service.ps1
```

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

## Nota Sobre el IDE

El aviso `Project configuration is not up-to-date with pom.xml` no es un error de ejecucion. Indica que el IDE aun no recargo Maven despues de cambiar el `pom.xml`.

Acciones recomendadas:

- VS Code: ejecutar `Java: Update Project Configuration`.
- VS Code: ejecutar `Maven: Reload All Maven Projects`.
- Si persiste: ejecutar `Java: Clean Java Language Server Workspace`.
- IntelliJ IDEA: `Reload All Maven Projects`.
- Eclipse/STS: `Maven > Update Project`.
