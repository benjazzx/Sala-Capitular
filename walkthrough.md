# Walkthrough — Sala Capitular

Guía de extremo a extremo para validar el sistema completo tras los cambios de esta rama.

## Parte A — Validación local (sin Docker)

### 1. Preparar bases de datos
En phpMyAdmin (XAMPP) ejecuta el script de [docs/DOCKER_GUIDE](docs/DOCKER_GUIDE.md) o las sentencias `CREATE DATABASE` del README (11 bases).

### 2. Compilar y testear cada servicio
Desde la carpeta de cada microservicio:
```bash
mvn clean verify
```
Esto compila, corre los tests (Service + Controller sobre H2) y genera el reporte JaCoCo en `target/site/jacoco/index.html`.

### 3. Arrancar en orden
Respeta el orden por dependencias Feign:
```
Rol → Catalogo → Estado → User → Libro → Estante → Historial → Multas → Detalle → ReseñaLibro → ReservaLibro
```
Para cada uno:
```bash
mvn spring-boot:run
```

### 4. Verificar Swagger
Abre, por ejemplo:
```
http://localhost:8081/swagger-ui/index.html   (Rol)
http://localhost:8085/swagger-ui/index.html   (Libro)
```

### 5. Arrancar el gateway
```bash
cd api-gateway
mvn spring-boot:run
```
Verifica las rutas:
```
http://localhost:8080/actuator/gateway/routes
```

### 6. Probar a través del gateway (Postman)
- Login: `POST http://localhost:8080/api/users/login`
  ```json
  { "email": "admin@biblioteca.cl", "password": "admin123" }
  ```
- Listar libros: `GET http://localhost:8080/api/libros`
- Crear reserva: `POST http://localhost:8080/api/reservas`
  ```json
  { "userId": 5, "libroId": 1, "fechaReserva": "2026-06-12", "estadoReserva": "ACTIVA" }
  ```

## Parte B — Validación con Docker

### 1. Variables de entorno
```bash
cp .env.example .env
```

### 2. Levantar todo
```bash
docker compose up --build
```
La primera vez compila los 12 módulos; espera a que el gateway quede arriba.

### 3. Verificaciones
- Salud del gateway: `http://localhost:8080/actuator/health` → `{"status":"UP"}`
- Rutas: `http://localhost:8080/actuator/gateway/routes`
- Flujo completo de Postman apuntando a `http://localhost:8080/api/...`

### 4. Apagar
```bash
docker compose down        # conserva datos
docker compose down -v      # borra también el volumen MySQL
```

## Checklist de validación final

- [ ] `mvn clean verify` pasa en los 11 servicios
- [ ] Reporte JaCoCo generado por servicio
- [ ] Swagger UI accesible en cada servicio
- [ ] Gateway enruta correctamente (`/actuator/gateway/routes`)
- [ ] `docker compose up --build` levanta todo el stack
- [ ] Flujo login → listar → reservar funciona vía gateway
- [ ] Regla de bloqueo por multas se respeta (usuario con >3 puntos no puede reservar)
