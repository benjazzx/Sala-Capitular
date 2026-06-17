# Sistema de Biblioteca — Arquitectura de Microservicios

**Rama de entrega:** `feature/cierre-sumativa-microservicios`
**Estado:** ✅ Compilación exitosa · Java 21 · Spring Boot 4.0.7

---

## Descripción

Sistema de gestión de biblioteca desarrollado con arquitectura de microservicios independientes usando Spring Boot, Java 21 y comunicación entre servicios mediante Feign Client. Cada microservicio posee su propia base de datos, lógica de negocio y endpoints REST completos. Incluye Eureka Server para service discovery, API Gateway con enrutamiento dinámico (`lb://`) y soporte Docker Compose.

## Integrantes del equipo

| Nombre | Rol |
|--------|-----|
| Benjamin Araneda | Desarrollador |
| Gabriel Castro | Desarrollador |
| Felipe Lara | Desarrollador |

---

## Tecnologías

- Java 21
- Spring Boot 4.0.7
- Spring Cloud 2025.1.2 (Feign, Gateway MVC, Eureka, LoadBalancer)
- Spring Data JPA + Hibernate
- MySQL (XAMPP) / H2 (tests)
- Netflix Eureka Server (service discovery)
- Spring Cloud Gateway MVC (API Gateway)
- springdoc-openapi 3.0.3 (Swagger UI / OpenAPI)
- JUnit 5 + Mockito + AssertJ + JaCoCo 0.8.13
- Docker + Docker Compose
- Maven Wrapper

---

## Microservicios

| Servicio | Puerto | Base de datos | Nombre Eureka | Swagger |
|----------|--------|---------------|---------------|---------|
| Rol | 8081 | db_rol | `rol-service` | http://localhost:8081/swagger-ui/index.html |
| User | 8082 | db_user | `user-service` | http://localhost:8082/swagger-ui/index.html |
| Catalogo | 8083 | db_catalogo | `catalogo-service` | http://localhost:8083/swagger-ui/index.html |
| Estado | 8084 | db_estado | `estado-service` | http://localhost:8084/swagger-ui/index.html |
| Libro | 8085 | db_libro | `libro-service` | http://localhost:8085/swagger-ui/index.html |
| Estante | 8086 | db_estante | `estante-service` | http://localhost:8086/swagger-ui/index.html |
| Historial | 8087 | db_historial | `historial-service` | http://localhost:8087/swagger-ui/index.html |
| Multas | 8088 | db_multas | `multas-service` | http://localhost:8088/swagger-ui/index.html |
| ReservaLibro | 8089 | db_reserva_libro | `reserva-libro-service` | http://localhost:8089/swagger-ui/index.html |
| Detalle | 8090 | db_detalle | `detalle-service` | http://localhost:8090/swagger-ui/index.html |
| ReseñaLibro | 8091 | db_resena_libro | `resena-libro-service` | http://localhost:8091/swagger-ui/index.html |
| API Gateway | 8080 | — | `api-gateway` | — |
| Eureka Server | 8761 | — | — | http://localhost:8761 |

---

## Requisitos previos

- Java 21 instalado (`java -version`)
- Maven Wrapper incluido en cada microservicio (`mvnw.cmd` / `./mvnw`)
- MySQL corriendo en puerto 3306 (XAMPP o Docker)
- Docker Desktop (para ejecución con Docker Compose)
- Git

---

## Ejecución manual (uno por uno)

### 1. Crear bases de datos en MySQL

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

### 2. Iniciar Eureka Server (primero)

```powershell
cd eurekaserver
.\mvnw.cmd spring-boot:run
```

Verificar en: http://localhost:8761

### 3. Iniciar microservicios (en orden)

```powershell
# Servicios base (sin dependencias entre sí)
cd Rol\Rol        && .\mvnw.cmd spring-boot:run
cd Catalogo\Catalogo && .\mvnw.cmd spring-boot:run
cd Estado\Estado  && .\mvnw.cmd spring-boot:run

# User depende de Rol
cd User\User      && .\mvnw.cmd spring-boot:run

# Libro depende de Catalogo, Estado, User
cd Libro\Libro    && .\mvnw.cmd spring-boot:run

# Nivel siguiente
cd Estante\Estante     && .\mvnw.cmd spring-boot:run
cd Historial\Historial && .\mvnw.cmd spring-boot:run
cd Multas\Multas       && .\mvnw.cmd spring-boot:run

# Nivel final
cd Detalle\Detalle           && .\mvnw.cmd spring-boot:run
cd ReseñaLibro\ReseñaLibro   && .\mvnw.cmd spring-boot:run
cd ReservaLibro\ReservaLibro && .\mvnw.cmd spring-boot:run
```

En Linux/macOS reemplazar `.\mvnw.cmd` por `./mvnw`.

### 4. Iniciar API Gateway (último)

```powershell
cd api-gateway
.\mvnw.cmd spring-boot:run
```

El gateway requiere que Eureka esté corriendo y al menos un servicio registrado. Acceso unificado: http://localhost:8080/api/...

---

## Ejecución con Docker Compose

Todo el sistema (MySQL + Eureka + 11 servicios + gateway) se levanta con un solo comando:

```bash
cp .env.example .env
docker compose up --build
```

Verificar contenedores:
```bash
docker ps
docker images
```

URLs una vez levantado:
- Eureka:  http://localhost:8761
- Gateway: http://localhost:8080/api/roles

Guía completa en [`docs/DOCKER_GUIDE.md`](docs/DOCKER_GUIDE.md).

---

## Eureka Server

El Eureka Server actúa como registro de descubrimiento. Todos los microservicios se registran automáticamente al arrancar. El API Gateway usa las URIs `lb://nombre-servicio` para resolver instancias vía Eureka.

Consola de administración: http://localhost:8761

Guía completa en [`docs/EUREKA_GUIDE.md`](docs/EUREKA_GUIDE.md).

---

## API Gateway

El API Gateway (Spring Cloud Gateway MVC, puerto **8080**) enruta por path hacia los 11 microservicios usando Eureka Service Discovery:

| Path | Servicio |
|------|----------|
| `/api/roles/**` | `lb://rol-service` |
| `/api/users/**` | `lb://user-service` |
| `/api/catalogos/**` | `lb://catalogo-service` |
| `/api/estados/**` | `lb://estado-service` |
| `/api/libros/**` | `lb://libro-service` |
| `/api/estantes/**` | `lb://estante-service` |
| `/api/historiales/**` | `lb://historial-service` |
| `/api/multas/**` | `lb://multas-service` |
| `/api/reservas/**` | `lb://reserva-libro-service` |
| `/api/detalles/**` | `lb://detalle-service` |
| `/api/resenas/**` | `lb://resena-libro-service` |

Plan y detalles en [`docs/API_GATEWAY_PLAN.md`](docs/API_GATEWAY_PLAN.md).

---

## Swagger / OpenAPI

Cada microservicio expone:
- Swagger UI: `http://localhost:{puerto}/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:{puerto}/v3/api-docs`

Documentación completa en [`docs/API_DOCUMENTATION.md`](docs/API_DOCUMENTATION.md).

---

## Pruebas unitarias y JaCoCo

```powershell
cd <carpeta-del-microservicio>
.\mvnw.cmd clean verify
# Reporte: target\site\jacoco\index.html
```

Cobertura mínima alcanzada: **80% en todos los microservicios** (rango real: 88%–98%).

| Microservicio | Cobertura |
|--------------|-----------|
| Rol | 96%+ |
| User | 94%+ |
| Catalogo | 96%+ |
| Estado | 89%+ |
| Libro | 90%+ |
| Estante | 88%+ |
| Historial | 92%+ |
| Multas | 91%+ |
| ReservaLibro | 88%+ |
| Detalle | 92%+ |
| ReseñaLibro | 93%+ |

Estrategia en [`docs/TESTING_STRATEGY.md`](docs/TESTING_STRATEGY.md). Reporte en [`docs/COVERAGE_REPORT.md`](docs/COVERAGE_REPORT.md).

---

## Reglas de negocio

- **Roles**: Solo usuarios con rol `AUTOR` pueden ser asignados como autores de un libro.
- **Multas**: Si la suma de puntos supera 3, el usuario queda bloqueado para reservar.
- **Reservas**: No se pueden crear reservas `ACTIVA` duplicadas para el mismo libro.
- **Admin multa**: Solo un usuario con rol `ADMIN` puede registrar multas.
- **Login**: Usuarios (ADMIN, AUTOR, CLIENTE) pueden autenticarse con email y contraseña.

---

## Credenciales de prueba

| Email | Contraseña | Rol |
|-------|-----------|-----|
| admin@biblioteca.cl | admin123 | ADMIN |
| gabriela.mistral@biblioteca.cl | autor123 | AUTOR |
| juan.perez@biblioteca.cl | cliente123 | CLIENTE |

---

## Comandos útiles

```powershell
# Compilar y verificar cobertura
.\mvnw.cmd clean verify

# Solo ejecutar tests
.\mvnw.cmd test

# Levantar servicio
.\mvnw.cmd spring-boot:run

# Construir JAR
.\mvnw.cmd clean package -DskipTests

# Docker Compose
docker compose up --build -d
docker compose down
docker compose logs -f <servicio>
```

---

## Troubleshooting

| Problema | Solución |
|---------|----------|
| Puerto ocupado | `netstat -ano \| findstr :<puerto>` → `taskkill /PID <PID> /F` |
| Microservicio no arranca | Verificar MySQL activo y base de datos creada |
| Gateway devuelve 503 | Esperar ~30s para que Eureka registre el servicio |
| Servicio no aparece en Eureka | Verificar `eureka.client.service-url.defaultZone` en application.properties |
| Tests fallan con DB error | Tests usan perfil H2 en memoria — no requieren MySQL |
| Docker build falla | Verificar que `mvnw` tenga permisos: `chmod +x mvnw` |

---

## Documentación adicional

| Documento | Contenido |
|-----------|-----------|
| [docs/DIAGNOSTICO_FINAL.md](docs/DIAGNOSTICO_FINAL.md) | Diagnóstico real del estado de todos los microservicios |
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | Arquitectura y diagrama de dependencias |
| [docs/EUREKA_GUIDE.md](docs/EUREKA_GUIDE.md) | Guía Eureka Server y registro de clientes |
| [docs/API_GATEWAY_PLAN.md](docs/API_GATEWAY_PLAN.md) | Diseño del API Gateway |
| [docs/API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md) | Endpoints y ejemplos JSON |
| [docs/TESTING_STRATEGY.md](docs/TESTING_STRATEGY.md) | Estrategia de pruebas unitarias |
| [docs/COVERAGE_REPORT.md](docs/COVERAGE_REPORT.md) | Reporte de cobertura JaCoCo |
| [docs/DOCKER_GUIDE.md](docs/DOCKER_GUIDE.md) | Guía Docker y Docker Compose |
| [docs/VERIFICATION_CHECKLIST.md](docs/VERIFICATION_CHECKLIST.md) | Lista de verificación por microservicio |
| [docs/FINAL_DELIVERY_REPORT.md](docs/FINAL_DELIVERY_REPORT.md) | Reporte final de entrega |
| [docs/ROADMAP.md](docs/ROADMAP.md) | Mejoras futuras |

## Repositorio

[https://github.com/benjazzx/Sala-Capitular](https://github.com/benjazzx/Sala-Capitular)
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                