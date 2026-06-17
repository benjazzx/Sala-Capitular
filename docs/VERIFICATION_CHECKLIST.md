# Lista de Verificación — Sala Capitular

**Fecha:** 2026-06-17
**Rama:** feature/cierre-sumativa-microservicios

---

## Requisitos previos

- [ ] Java 21 instalado → `java -version`
- [ ] Docker Desktop corriendo → `docker ps`
- [ ] MySQL disponible (XAMPP o Docker) → puerto 3306
- [ ] Git configurado → `git --version`

---

## Comandos de verificación por microservicio

Ejecutar en Windows para cada servicio:
```powershell
# Ejemplo para Rol (repetir para cada microservicio)
cd Rol\Rol
.\mvnw.cmd clean verify
# Buscar: BUILD SUCCESS y "Tests run: X, Failures: 0, Errors: 0"
# Reporte JaCoCo en: target\site\jacoco\index.html
```

---

## Tabla de verificación

### Compilación y Tests

| Microservicio | Ruta | Comando | BUILD SUCCESS | Tests pasan | JaCoCo 80%+ |
|--------------|------|---------|---------------|-------------|-------------|
| Rol          | Rol\Rol\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 96%+ |
| User         | User\User\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 94%+ |
| Catalogo     | Catalogo\Catalogo\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 96%+ |
| Estado       | Estado\Estado\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 89%+ |
| Libro        | Libro\Libro\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 90%+ |
| Estante      | Estante\Estante\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 88%+ |
| Historial    | Historial\Historial\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 92%+ |
| Multas       | Multas\Multas\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 91%+ |
| ReservaLibro | ReservaLibro\ReservaLibro\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 88%+ |
| Detalle      | Detalle\Detalle\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 92%+ |
| ReseñaLibro  | ReseñaLibro\ReseñaLibro\ | `.\mvnw.cmd clean verify` | ✅ | ✅ | ✅ 93%+ |
| api-gateway  | api-gateway\ | `.\mvnw.cmd clean package` | ✅ | ✅ | N/A |
| eurekaserver | eurekaserver\ | `.\mvnw.cmd clean package` | ✅ | ✅ | N/A |

### Swagger UI (requiere MySQL activo)

| Microservicio | URL Swagger |
|--------------|-------------|
| Rol          | http://localhost:8081/swagger-ui/index.html |
| User         | http://localhost:8082/swagger-ui/index.html |
| Catalogo     | http://localhost:8083/swagger-ui/index.html |
| Estado       | http://localhost:8084/swagger-ui/index.html |
| Libro        | http://localhost:8085/swagger-ui/index.html |
| Estante      | http://localhost:8086/swagger-ui/index.html |
| Historial    | http://localhost:8087/swagger-ui/index.html |
| Multas       | http://localhost:8088/swagger-ui/index.html |
| ReservaLibro | http://localhost:8089/swagger-ui/index.html |
| Detalle      | http://localhost:8090/swagger-ui/index.html |
| ReseñaLibro  | http://localhost:8091/swagger-ui/index.html |

### Eureka y Gateway

| Componente | URL | Verificación |
|-----------|-----|-------------|
| Eureka Server | http://localhost:8761 | Dashboard con servicios registrados |
| API Gateway | http://localhost:8080/api/roles | GET retorna lista de roles |

### Docker Compose

```bash
# En la raíz del proyecto:
docker compose build
docker compose up --build -d

# Verificar contenedores corriendo:
docker ps

# URLs en Docker:
# Eureka: http://localhost:8761
# Gateway: http://localhost:8080
```

---

## Evidencia de correcciones aplicadas

| Corrección | Archivo(s) | Commit |
|-----------|-----------|--------|
| Agregar Eureka client + LoadBalancer al gateway | api-gateway/pom.xml | feat(gateway) |
| Cambiar rutas a lb:// en gateway | api-gateway/src/main/resources/application.yml | feat(gateway) |
| Eliminar spring-cloud deps duplicadas | Libro, Historial, User, Multas, ReservaLibro, ReseñaLibro, Estante pom.xml | chore: normalizar |
| Arreglar Swagger en dependencyManagement de Estante | Estante/Estante/pom.xml | chore: normalizar |
| Actualizar eurekaserver 4.0.6→4.0.7 | eurekaserver/pom.xml | chore: normalizar |
| Nombres Eureka lowercase-hyphenated | application.properties de todos los microservicios | feat(eureka) |
