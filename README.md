# Sistema de Biblioteca - Arquitectura de Microservicios

**Estado:** ✅ **Compilación Exitosa con Java 21**

## Descripción

Sistema de gestión de biblioteca desarrollado con arquitectura de microservicios independientes usando Spring Boot, Java 21 y comunicación entre servicios mediante Feign Client. Cada microservicio posee su propia base de datos, lógica de negocio y endpoints REST completos.

## Integrantes del equipo

| Nombre | Rol |
|--------|-----|
| Benjamin Araneda | Desarrollador |
| Gabriel Castro | Desarrollador |
| Felipe Lara | Desarrollador |

## ⚡ Quick Start

### 1. Validar todos los servicios
```powershell
cd d:\workspace\Duoc\Sala-Capitular
.\build-all.ps1
```

### 2. Ejecutar un servicio
```powershell
.\run-service.ps1 Rol
.\run-service.ps1 Libro
```

### 3. Ver API Swagger
```
http://localhost:8081/swagger-ui/index.html  (Rol)
http://localhost:8085/swagger-ui/index.html  (Libro)
http://localhost:8089/swagger-ui/index.html  (ReservaLibro)
```

### 4. Ejecutar tests con cobertura
```powershell
.\build-all.ps1
```

**Documentación completa:** Ver [docs/ESTADO_ACTUAL.md](docs/ESTADO_ACTUAL.md)

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.0.7
- Spring Cloud 2025.1.1 (Feign Client + Gateway MVC)
- Spring Data JPA + Hibernate
- MySQL (XAMPP) / H2 (tests)
- Lombok
- Bean Validation (JSR 380)
- SLF4J (logging)
- springdoc OpenAPI / Swagger UI
- JUnit 5 + Mockito + JaCoCo
- Docker + Docker Compose
- Maven

## Microservicios implementados

| Servicio | Puerto | Base de datos | Feign hacia |
|----------|--------|---------------|-------------|
| Rol | 8081 | db_rol | — |
| User | 8082 | db_user | Rol |
| Catalogo | 8083 | db_catalogo | — |
| Estado | 8084 | db_estado | — |
| Libro | 8085 | db_libro | Catalogo, Estado, User |
| Estante | 8086 | db_estante | Libro |
| Historial | 8087 | db_historial | Libro, User, Estado |
| Multas | 8088 | db_multas | User, Historial |
| ReservaLibro | 8089 | db_reserva_libro | Libro, User, Multas |
| Detalle | 8090 | db_detalle | Historial, Libro |
| ReseñaLibro | 8091 | db_resena_libro | Libro, User |

## Reglas de negocio

- **Roles**: Solo usuarios con rol `AUTOR` pueden ser asignados como autores de un libro.
- **Multas**: Si la suma de puntos de multas de un usuario supera 3, queda bloqueado para reservar. Al intentar reservar con multas activas, la respuesta incluye el detalle de sus multas.
- **Reservas**: No se pueden crear reservas duplicadas (`ACTIVA`) para el mismo libro.
- **Admin multa**: Solo un usuario con rol `ADMIN` puede registrar multas. Las multas tienen tipo: `NO_ENTREGA`, `DAÑO` o `RETRASO`.
- **Login**: Los usuarios (ADMIN, AUTOR, CLIENTE) pueden iniciar sesión con email y contraseña.

## Funcionalidades implementadas

- CRUD completo en los 11 microservicios
- Validaciones con Bean Validation en todos los DTOs
- Manejo centralizado de errores con `@RestControllerAdvice` (GlobalExceptionHandler)
- Comunicación entre microservicios con Feign Client
- Logs estructurados con SLF4J en toda la capa de servicio (info, warn, error)
- Datos precargados automáticamente al iniciar cada servicio (DataInitializer)
- Login por email y contraseña
- Aviso de multas al momento de intentar reservar un libro
- Filtro de multas por tipo y endpoint `/no-entrega` para el administrador

## Datos precargados por defecto

| Servicio | Datos |
|----------|-------|
| Rol | CLIENTE, ADMIN, AUTOR |
| Catalogo | 7 géneros literarios |
| Estado | DISPONIBLE, RESERVADO, PRESTADO, DAÑADO |
| User | 1 admin, 3 autores, 3 clientes |
| Libro | 7 libros clásicos |
| Estante | 7 estantes (pasillo + nivel) |
| Historial | 5 préstamos |
| Multas | 3 multas |
| ReservaLibro | 4 reservas |
| Detalle | 5 detalles |
| ReseñaLibro | 6 reseñas |

## Credenciales de prueba

| Email | Contraseña | Rol |
|-------|-----------|-----|
| admin@biblioteca.cl | admin123 | ADMIN |
| gabriela.mistral@biblioteca.cl | autor123 | AUTOR |
| juan.perez@biblioteca.cl | cliente123 | CLIENTE |

## Pasos para ejecutar

### 1. Requisitos previos

- Java 21 instalado
- XAMPP con MySQL corriendo en puerto 3306
- Maven Wrapper incluido en cada microservicio (`mvnw.cmd` en Windows, `./mvnw` en Linux/macOS)

### 2. Crear bases de datos en MySQL

Ejecutar en phpMyAdmin o consola MySQL:

```sql
CREATE DATABASE db_rol;
CREATE DATABASE db_user;
CREATE DATABASE db_catalogo;
CREATE DATABASE db_estado;
CREATE DATABASE db_libro;
CREATE DATABASE db_estante;
CREATE DATABASE db_historial;
CREATE DATABASE db_multas;
CREATE DATABASE db_reserva_libro;
CREATE DATABASE db_detalle;
CREATE DATABASE db_resena_libro;
```

### 3. Iniciar los microservicios en orden

Los microservicios deben iniciarse en el siguiente orden debido a las dependencias entre ellos:

```
1. Rol          (puerto 8081)
2. Catalogo     (puerto 8083)
3. Estado       (puerto 8084)
4. User         (puerto 8082)
5. Libro        (puerto 8085)
6. Estante      (puerto 8086)
7. Historial    (puerto 8087)
8. Multas       (puerto 8088)
9. Detalle      (puerto 8090)
10. ReseñaLibro (puerto 8091)
11. ReservaLibro (puerto 8089)
12. API Gateway (puerto 8080)
```

Para cada microservicio:

```bash
cd <carpeta-del-microservicio>
.\mvnw.cmd spring-boot:run
```

En Linux/macOS usa `./mvnw spring-boot:run`.

También puedes ejecutar desde la raíz del proyecto con Java 21 local:

```powershell
.\run-service.ps1 Rol
.\run-service.ps1 Catalogo
.\run-service.ps1 Estado
```

### 4. Verificar que los datos se precargaron

```
GET http://localhost:8081/api/roles
GET http://localhost:8082/api/users
GET http://localhost:8085/api/libros
```

## Endpoints principales por microservicio

### Rol (8081)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | /api/roles | Listar todos |
| GET | /api/roles/{id} | Buscar por id |
| POST | /api/roles | Crear |
| PUT | /api/roles/{id} | Actualizar |
| DELETE | /api/roles/{id} | Eliminar |

### User (8082)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | /api/users | Listar todos |
| GET | /api/users/{id} | Buscar por id |
| POST | /api/users | Crear |
| POST | /api/users/login | Iniciar sesión |
| PUT | /api/users/{id} | Actualizar |
| DELETE | /api/users/{id} | Eliminar |

### Multas (8088)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | /api/multas | Listar todas |
| GET | /api/multas/{id} | Buscar por id |
| GET | /api/multas/usuario/{userId} | Multas de un usuario |
| GET | /api/multas/usuario/{userId}/estado | Estado detallado con aviso |
| GET | /api/multas/usuario/{userId}/puede-pedir | Verificar si puede reservar |
| GET | /api/multas/no-entrega | Filtrar por NO_ENTREGA |
| GET | /api/multas/tipo/{tipo} | Filtrar por tipo |
| POST | /api/multas | Registrar multa (requiere adminId con rol ADMIN) |
| PUT | /api/multas/{id} | Actualizar |
| DELETE | /api/multas/{id} | Eliminar |

### ReservaLibro (8089)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | /api/reservas | Listar todas |
| GET | /api/reservas/{id} | Buscar por id |
| POST | /api/reservas | Crear reserva (incluye aviso de multas en respuesta) |
| PUT | /api/reservas/{id} | Actualizar |
| DELETE | /api/reservas/{id} | Eliminar |

> Los demás servicios (Catalogo, Estado, Libro, Estante, Historial, Detalle, ReseñaLibro) siguen el mismo patrón CRUD estándar en sus respectivos puertos.

## Estructura de cada microservicio

```
src/main/java/Biblioteca/example/<Servicio>/
├── config/
│   └── DataInitializer.java      ← datos precargados al iniciar
├── model/
│   └── <Entidad>.java            ← @Entity JPA
├── dto/
│   ├── <Entidad>RequestDTO.java  ← validaciones Bean Validation
│   └── <Entidad>ResponseDTO.java ← respuesta al cliente
├── repository/
│   └── <Entidad>Repository.java  ← extends JpaRepository
├── service/
│   └── <Entidad>Service.java     ← lógica + logs SLF4J
├── controller/
│   └── <Entidad>Controller.java  ← endpoints REST
├── client/
│   └── <Otro>Client.java         ← @FeignClient (si consume otros)
└── exception/
    └── GlobalExceptionHandler.java ← manejo centralizado de errores
```

## Repositorio

[https://github.com/benjazzx/Sala-Capitular](https://github.com/benjazzx/Sala-Capitular)

## Documentación API (Swagger)

Cada microservicio expone Swagger UI y el contrato OpenAPI:

```
http://localhost:{puerto}/swagger-ui/index.html
http://localhost:{puerto}/v3/api-docs
```

Detalle completo de endpoints en [`docs/API_DOCUMENTATION.md`](docs/API_DOCUMENTATION.md).

## Tests y cobertura

Cada servicio incluye pruebas unitarias de servicio y de controlador (JUnit 5 + Mockito + MockMvc) y mide cobertura con JaCoCo sobre una base H2 en memoria (perfil `test`).

```bash
cd <carpeta-del-microservicio>
.\mvnw.cmd clean verify
# Reporte: target/site/jacoco/index.html
```

En Linux/macOS usa `./mvnw clean verify`.

Estrategia detallada en [`docs/TESTING_STRATEGY.md`](docs/TESTING_STRATEGY.md).

## API Gateway

Un API Gateway (Spring Cloud Gateway MVC) en el puerto **8080** enruta por path hacia los 11 servicios. Ejecutar:

```bash
cd api-gateway
.\mvnw.cmd spring-boot:run
# Acceso unificado: http://localhost:8080/api/...
```

Plan y tabla de rutas en [`docs/API_GATEWAY_PLAN.md`](docs/API_GATEWAY_PLAN.md).

## Ejecución con Docker

Todo el sistema (MySQL + 11 servicios + gateway) se levanta con Docker Compose:

```bash
cp .env.example .env
docker compose up --build
# Punto de entrada: http://localhost:8080/api/...
```

Guía completa en [`docs/DOCKER_GUIDE.md`](docs/DOCKER_GUIDE.md).

## Documentación adicional

| Documento | Contenido |
|---|---|
| [docs/DIAGNOSTICO_INICIAL.md](docs/DIAGNOSTICO_INICIAL.md) | Inventario, orden de arranque, problemas detectados |
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | Arquitectura y diagrama de dependencias |
| [docs/API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md) | Endpoints y ejemplos JSON |
| [docs/TESTING_STRATEGY.md](docs/TESTING_STRATEGY.md) | Estrategia de pruebas |
| [docs/COVERAGE_REPORT.md](docs/COVERAGE_REPORT.md) | Reporte de cobertura JaCoCo |
| [docs/API_GATEWAY_PLAN.md](docs/API_GATEWAY_PLAN.md) | Diseño del gateway |
| [docs/DOCKER_GUIDE.md](docs/DOCKER_GUIDE.md) | Guía Docker |
| [docs/ROADMAP.md](docs/ROADMAP.md) | Recomendaciones y mejoras futuras |
| [docs/CHANGELOG.md](docs/CHANGELOG.md) | Registro de cambios |

## Registro de colaboración

Se realizó una revisión general de la arquitectura del proyecto, considerando la organización por microservicios, comunicación mediante Feign Client y reglas de negocio principales.
