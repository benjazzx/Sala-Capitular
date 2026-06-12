# CHECKLIST DE VERIFICACIÓN - Sala Capitular

Usar este checklist para verificar que todo funciona correctamente.

## ✅ Pre-ejecución

- [ ] Java 21 instalado
  ```bash
  java -version
  # Resultado esperado: openjdk version "21.0.10"
  ```

- [ ] Maven disponible
  ```bash
  cd d:\workspace\Duoc\Sala-Capitular\Rol\Rol
  mvnw -version
  ```

- [ ] Todos los servicios compilados
  ```bash
  cd d:\workspace\Duoc\Sala-Capitular
  build-all.bat
  # Resultado esperado: "Compilados exitosamente: 11"
  ```

- [ ] MySQL disponible
  ```bash
  # XAMPP Control Panel: Start MySQL
  # O verificar:
  mysql -u root -p
  ```

## ✅ Ejecución Individual

### Rol (8081)
- [ ] Inicia correctamente
  ```bash
  run-service.bat Rol
  ```
  
- [ ] Swagger accesible
  ```
  http://localhost:8081/swagger-ui.html
  ```
  
- [ ] Health check
  ```bash
  curl http://localhost:8081/actuator/health
  ```

### Catalogo (8083)
- [ ] Inicia correctamente
- [ ] Swagger accesible: `http://localhost:8083/swagger-ui.html`
- [ ] Health check: `curl http://localhost:8083/actuator/health`

### Estado (8084)
- [ ] Inicia correctamente
- [ ] Swagger accesible: `http://localhost:8084/swagger-ui.html`
- [ ] Health check: `curl http://localhost:8084/actuator/health`

### User (8082) - Depende de Rol
- [ ] Rol está corriendo en 8081
- [ ] User inicia correctamente
- [ ] Swagger accesible: `http://localhost:8082/swagger-ui.html`
- [ ] Feign Client puede llamar a Rol

### Libro (8085) - Depende de Catalogo, Estado, User
- [ ] Catalogo corre en 8083
- [ ] Estado corre en 8084
- [ ] User corre en 8082
- [ ] Libro inicia correctamente
- [ ] Swagger accesible: `http://localhost:8085/swagger-ui.html`
- [ ] Feign Clients funcionan

### Resto de servicios
- [ ] Cada uno inicia sin errores
- [ ] Swagger accesible en su puerto
- [ ] Health check funciona

## ✅ Ejecución Completa (Todos los servicios)

- [ ] `run-all.bat` abre 11 ventanas
- [ ] Cada ventana muestra "Started Application in X seconds"
- [ ] Ningún servicio muestra errors en los logs
- [ ] Todos los puertos están accesibles

**Verificación rápida:**
```bash
# En otra terminal, verificar todos
for /L %i in (8081,1,8091) do curl -s http://localhost:%i/actuator/health | find "UP"
```

## ✅ Testing y Cobertura

- [ ] Tests ejecutan sin errores
  ```bash
  run-tests.bat
  ```

- [ ] Reportes JaCoCo generados
  ```
  Rol/Rol/target/site/jacoco/index.html
  Catalogo/Catalogo/target/site/jacoco/index.html
  ...
  ```

- [ ] Cobertura > 50% en servicios principales
  - [ ] Rol
  - [ ] Libro
  - [ ] User
  - [ ] Historial

## ✅ API Endpoints

### Rol API (8081)
- [ ] GET /api/roles
- [ ] POST /api/roles
- [ ] GET /api/roles/{id}
- [ ] PUT /api/roles/{id}
- [ ] DELETE /api/roles/{id}

### User API (8082)
- [ ] GET /api/usuarios
- [ ] POST /api/usuarios
- [ ] GET /api/usuarios/{id}
- [ ] PUT /api/usuarios/{id}
- [ ] DELETE /api/usuarios/{id}

### Libro API (8085)
- [ ] GET /api/libros
- [ ] POST /api/libros
- [ ] GET /api/libros/{id}
- [ ] PUT /api/libros/{id}
- [ ] DELETE /api/libros/{id}

### Historial API (8087)
- [ ] GET /api/historial
- [ ] POST /api/historial
- [ ] GET /api/historial/usuario/{usuarioId}

### ReservaLibro API (8089)
- [ ] GET /api/reservas
- [ ] POST /api/reservas
- [ ] GET /api/reservas/{id}
- [ ] PUT /api/reservas/{id}

## ✅ Documentación

- [ ] DIAGNOSTICO_INICIAL.md - Existe y es legible
- [ ] ESTADO_ACTUAL.md - Actualizado
- [ ] GUIA_EJECUCION.md - Completo
- [ ] ARCHITECTURE.md - Existe
- [ ] API_DOCUMENTATION.md - Existe
- [ ] TESTING_STRATEGY.md - Existe
- [ ] DOCKER_GUIDE.md - Existe

## ✅ Docker Compose (Opcional)

- [ ] Docker Desktop corriendo
- [ ] docker-compose.yml existe
- [ ] `docker-compose up` inicia todos los servicios
  ```bash
  docker-compose up
  ```

- [ ] Todos los servicios están "Up"
  ```bash
  docker ps
  ```

- [ ] MySQL disponible
  ```bash
  docker ps | grep mysql
  ```

- [ ] `docker-compose down` detiene todo correctamente

## ✅ Git y Control de Versiones

- [ ] Rama correcta: `feature/documentacion-tests-swagger-gateway-docker`
  ```bash
  git branch
  ```

- [ ] Commits realizados
  ```bash
  git log --oneline | head -20
  ```

- [ ] Cambios commiteados (sin cambios pendientes)
  ```bash
  git status
  ```

## 🎯 Próximos Pasos

- [ ] Revisar cobertura de tests
- [ ] Completar tests faltantes
- [ ] Implementar API Gateway si no existe
- [ ] Crear Pipeline CI/CD
- [ ] Documentar decisiones arquitectónicas

## 📊 Resumen Final

Fecha de verificación: _______________

Total de checks completados: _____ de 80+

Observaciones:
```
_________________________________________________________________

_________________________________________________________________

_________________________________________________________________
```

Revisado por: ___________________

Fecha: ___________________
