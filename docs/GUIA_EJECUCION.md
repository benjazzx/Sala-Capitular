# GUÍA DE EJECUCIÓN - Sala Capitular

Esta guía te ayudará a ejecutar todos los microservicios paso a paso.

## Requisitos Previos

✅ Java 21 instalado en: `C:\Users\Felipe\AppData\Local\jdks\jdk-21.0.10`
✅ Todos los servicios compilados exitosamente
✅ MySQL disponible (XAMPP u otro)

## Opción 1: Ejecutar un Servicio Individual

### Paso 1: Abrir Terminal

```bash
cd d:\workspace\Duoc\Sala-Capitular
```

### Paso 2: Ejecutar el servicio

```bash
# Ejecutar Rol (8081)
run-service.bat Rol

# O en otra terminal:
# Ejecutar Libro (8085)
run-service.bat Libro
```

### Paso 3: Verificar que está corriendo

```
Swagger:  http://localhost:8081/swagger-ui.html
Health:   http://localhost:8081/actuator/health
API:      http://localhost:8081/api/...
```

**Servicios disponibles:**
```
Rol         → 8081
User        → 8082
Catalogo    → 8083
Estado      → 8084
Libro       → 8085
Estante     → 8086
Historial   → 8087
Multas      → 8088
ReservaLibro → 8089
Detalle     → 8090
ReseñaLibro → 8091
```

---

## Opción 2: Ejecutar Todos los Servicios en Orden

### Paso 1: Ejecutar script

```bash
cd d:\workspace\Duoc\Sala-Capitular
run-all.bat
```

Esto abrirá 11 ventanas de terminal, una para cada servicio en el orden correcto:

**Fase 1 (0-5 seg):** Sin dependencias
- Rol (8081)
- Catalogo (8083)
- Estado (8084)

**Fase 2 (5-10 seg):** Dependencias simples
- User (8082) → Rol
- Libro (8085) → Catalogo, Estado, User

**Fase 3 (10-15 seg):** Dependencias múltiples
- Estante (8086) → Libro
- Historial (8087) → Libro, User, Estado
- Detalle (8090) → Historial, Libro

**Fase 4 (15+ seg):** Servicios finales
- Multas (8088) → User, Historial
- ReseñaLibro (8091) → Libro, User
- ReservaLibro (8089) → Libro, User, Multas

### Paso 2: Esperar a que todos estén "RUNNING"

Cada ventana mostrará:
```
2025-06-12 ... - INFO  ... : Started RolApplication in 3.5 seconds
2025-06-12 ... - INFO  ... : Started UserApplication in 4.2 seconds
...
```

### Paso 3: Acceder a los servicios

**Dashboard de servicios:**
```
http://localhost:8081/swagger-ui.html  (Rol)
http://localhost:8082/swagger-ui.html  (User)
http://localhost:8083/swagger-ui.html  (Catalogo)
http://localhost:8084/swagger-ui.html  (Estado)
http://localhost:8085/swagger-ui.html  (Libro)
http://localhost:8086/swagger-ui.html  (Estante)
http://localhost:8087/swagger-ui.html  (Historial)
http://localhost:8088/swagger-ui.html  (Multas)
http://localhost:8089/swagger-ui.html  (ReservaLibro)
http://localhost:8090/swagger-ui.html  (Detalle)
http://localhost:8091/swagger-ui.html  (ReseñaLibro)
```

---

## Opción 3: Ejecutar con Docker Compose

### Paso 1: Asegurar que Docker está corriendo

```bash
docker --version
docker-compose --version
```

### Paso 2: Iniciar servicios

```bash
cd d:\workspace\Duoc\Sala-Capitular
docker-compose up
```

### Paso 3: Verificar servicios

```bash
docker ps
```

Todos los servicios deberían estar corriendo en sus puertos asignados.

---

## Ejecución de Tests y Cobertura

### Ejecutar todos los tests

```bash
run-tests.bat
```

### Ejecutar tests de un servicio

```bash
cd d:\workspace\Duoc\Sala-Capitular\Rol\Rol
.\mvnw.cmd clean test
```

### Ver reporte de cobertura

```bash
# Después de ejecutar tests
d:\workspace\Duoc\Sala-Capitular\Rol\Rol\target\site\jacoco\index.html
```

---

## Testing Endpoints de Ejemplo

### 1. Crear un Rol

```bash
curl -X POST "http://localhost:8081/api/roles" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"ADMIN","descripcion":"Administrador"}'
```

### 2. Crear un Usuario

```bash
curl -X POST "http://localhost:8082/api/usuarios" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan","email":"juan@example.com","rolId":1}'
```

### 3. Crear un Libro

```bash
curl -X POST "http://localhost:8085/api/libros" \
  -H "Content-Type: application/json" \
  -d '{"titulo":"El Quijote","autorId":1,"catalogoId":1,"estadoId":1}'
```

---

## Troubleshooting

### ❌ "Puerto ya está en uso"

Si el puerto está siendo usado:

```bash
# Encontrar qué proceso está usando el puerto 8081
netstat -ano | findstr :8081

# Matar el proceso
taskkill /PID <PID> /F
```

### ❌ "JAVA_HOME not found"

Configurar Java 21:

```powershell
$env:JAVA_HOME = 'C:\Users\Felipe\AppData\Local\jdks\jdk-21.0.10'
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
java -version
```

### ❌ "Database connection refused"

Asegurar que MySQL está corriendo:

```bash
# En XAMPP: Start MySQL
# O en Docker:
docker-compose up mysql
```

### ❌ "mvnw.cmd: File not found"

Asegurar de estar en la carpeta correcta:

```bash
cd d:\workspace\Duoc\Sala-Capitular\Rol\Rol
.\mvnw.cmd spring-boot:run
```

### ❌ "Servicio no responde"

Verificar logs en la ventana del servicio. Si hay errores de dependencias, asegurar que los servicios dependientes están corriendo en este orden:

1. **Sin dependencias:** Rol, Catalogo, Estado
2. **Dependen de otros:** User, Libro, Estante, Historial, Detalle, Multas, ReseñaLibro, ReservaLibro

---

## Documentación

- [DIAGNOSTICO_INICIAL.md](docs/DIAGNOSTICO_INICIAL.md) - Problemas resueltos
- [ESTADO_ACTUAL.md](docs/ESTADO_ACTUAL.md) - Estado de compilación
- [API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md) - APIs disponibles
- [ARCHITECTURE.md](docs/ARCHITECTURE.md) - Arquitectura del sistema
- [TESTING_STRATEGY.md](docs/TESTING_STRATEGY.md) - Estrategia de testing
- [DOCKER_GUIDE.md](docs/DOCKER_GUIDE.md) - Guía Docker

---

## Monitoreo y Mantenimiento

### Ver logs de un servicio

Las ventanas del CMD mostrarán los logs en tiempo real.

### Detener un servicio

`Ctrl+C` en su ventana del terminal

### Detener todos los servicios

Cerrar todas las ventanas o ejecutar:

```bash
# En Docker
docker-compose down
```

---

¿Preguntas? Revisar los documentos en `docs/` o ejecutar:

```bash
run-service.bat  # Ver uso
```
