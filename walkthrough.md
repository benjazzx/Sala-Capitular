# Walkthrough - Sala Capitular

Guia de extremo a extremo para validar el sistema completo en ejecucion local con XAMPP y, luego, con Docker.

## Parte A - Validacion local con XAMPP

### 1. Preparar bases de datos

Inicia XAMPP y deja MySQL corriendo en el puerto `3306`. Luego crea las 11 bases en phpMyAdmin o consola MySQL:

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

### 2. Compilar y testear cada servicio

Desde la carpeta de cada microservicio:

```powershell
.\mvnw.cmd clean verify
```

Esto compila, corre los tests de servicio/controlador con H2 y genera JaCoCo en `target/site/jacoco/index.html`.

### 3. Arrancar en orden

Respeta este orden por dependencias Feign:

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
12. API Gateway (8080)

Para cada microservicio:

```powershell
.\mvnw.cmd spring-boot:run
```

Para el gateway:

```powershell
cd api-gateway
.\mvnw.cmd spring-boot:run
```

### 4. Verificar Swagger/OpenAPI

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

### 5. Probar a traves del gateway

```text
GET  http://localhost:8080/api/roles
GET  http://localhost:8080/api/libros
POST http://localhost:8080/api/users/login
```

Body de login:

```json
{ "email": "admin@biblioteca.cl", "password": "admin123" }
```

Crear reserva:

```text
POST http://localhost:8080/api/reservas
```

```json
{
  "userId": 5,
  "libroId": 1,
  "fechaReserva": "2026-06-12",
  "estadoReserva": "ACTIVA"
}
```

## Parte B - Validacion con Docker

### 1. Variables de entorno

```powershell
copy .env.example .env
```

### 2. Levantar todo

```powershell
docker compose up --build
```

La primera vez compila los 12 modulos y crea las bases MySQL desde `docker/init/01-create-databases.sql`.

### 3. Verificaciones

- Gateway: `http://localhost:8080/actuator/health`
- Rutas: `http://localhost:8080/actuator/gateway/routes`
- API: `http://localhost:8080/api/...`
- Swagger directo por servicio: `http://localhost:{puerto}/swagger-ui/index.html`

### 4. Apagar

```powershell
docker compose down
docker compose down -v
```

## Checklist de validacion final

- [x] `.\mvnw.cmd clean verify` pasa en los 11 microservicios.
- [x] `.\mvnw.cmd clean verify` pasa en `api-gateway`.
- [x] JaCoCo generado por servicio.
- [x] Todos los servicios superan 90% de cobertura de instrucciones.
- [x] OpenAPI/Swagger configurado en los 11 servicios.
- [x] Gateway MVC configurado para enrutar `/api/...`.
- [x] Docker Compose y Dockerfiles creados para la siguiente fase de ejecucion eficiente.
- [ ] Verificacion manual con XAMPP levantando los 12 procesos en terminales separadas.
- [ ] Verificacion manual de `docker compose up --build` con Docker Desktop.
