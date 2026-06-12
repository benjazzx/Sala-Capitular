## Imported Claude Cowork project instructions

Primero, revisa todas estás tareas pendientes: Task Tracker — Sala-Capitular
Fase 0 — Rama de Trabajo

* Crear rama `feature/documentacion-tests-swagger-gateway-docker`
Fase 1 — Diagnóstico Inicial

* Verificar Java y Maven instalados
* Renombrar carpetas problemáticas (Rol (4), Estado (1))
* Compilar cada microservicio (`mvn clean compile -DskipTests`)
* Crear `docs/DIAGNOSTICO_INICIAL.md`
* Commit diagnóstico
Fase 2 — Swagger/OpenAPI

* Limpiar archivos duplicados en src/test (Estante, Multas)
* Agregar dependencia Swagger a: Rol, User, Estado, Libro, Historial, ReservaLibro, Detalle, ReseñaLibro
* Agregar OpenApiConfig y anotaciones de controller a todos
* Crear `docs/API_DOCUMENTATION.md`
* Commits Swagger
Fase 3 — JaCoCo

* Agregar jacoco-maven-plugin a los 11 pom.xml
* Agregar application-test.properties con H2 a todos
* Crear `docs/TESTING_STRATEGY.md`
* Commit JaCoCo
Fase 4 — Tests de Service

* Rol ServiceTest
* User ServiceTest
* Catalogo ServiceTest (ya existe, revisar)
* Estado ServiceTest
* Libro ServiceTest
* Estante ServiceTest
* Historial ServiceTest
* Multas ServiceTest (ya existe, revisar)
* ReservaLibro ServiceTest
* Detalle ServiceTest
* ReseñaLibro ServiceTest
* Commits por microservicio
Fase 5 — Tests de Controller

* Rol ControllerTest
* User ControllerTest
* Catalogo ControllerTest
* Estado ControllerTest
* Libro ControllerTest
* Estante ControllerTest
* Historial ControllerTest
* Multas ControllerTest
* ReservaLibro ControllerTest
* Detalle ControllerTest
* ReseñaLibro ControllerTest
* Commits por microservicio
Fase 6 — Tests de Excepciones

* Cubrir GlobalExceptionHandler en tests de controller
* Commit excepciones
Fase 7 — Validar Cobertura

* `mvn clean verify` en cada microservicio
* Crear `docs/COVERAGE_REPORT.md`
* Commit reporte
Fase 8 — Documentación

* Actualizar README.md
* Crear docs/ARCHITECTURE.md
* Crear docs/DOCKER_GUIDE.md
* Crear docs/API_GATEWAY_PLAN.md
* Crear docs/CHANGELOG.md
* Commit docs
Fase 10 — API Gateway

* Crear módulo api-gateway
* Configurar rutas
* Commit gateway
Fase 11 — Docker

* Dockerfiles por microservicio
* docker-compose.yml
* .env.example
* Completar docs/DOCKER_GUIDE.md
* Commit docker
Fase 12 — Recomendaciones

* docs/ROADMAP.md
* Commit recomendaciones
Validación Final

* `mvn clean verify` en todos
* Verificar Swagger funciona
* Verificar Gateway
* Verificar Docker Compose
* Crear walkthrough.md
Sala Capitular Microservices Enhancement
He creado el plan de implementación completo. Revísalo y responde las preguntas abiertas antes de que comience la ejecución. Los puntos clave que necesito que confirmes:

1. ¿Renombrar carpetas `Rol (4)` → `Rol`, `Estado (1)` → `Estado`? (facilita Docker y scripts)
2. ¿Qué hacer con los `ApplicationTests`? — Agregar H2 para perfil test, o deshabilitarlos con `@Disabled`
3. ¿Los archivos duplicados en `src/test/` de Estante y Multas se pueden eliminar? (son copia del controller/DTOs, no tests reales)
4. ¿Java 21 y Maven están instalados en este equipo?
5. ¿MySQL/XAMPP disponible para verificación final?
6. ¿Docker Desktop instalado?
7. ¿API Gateway MVC o WebFlux? (recomiendo MVC por consistencia)
Implementation Plan
2:14 PM
1. ok, 2. ok, 3. ok, 4. si, 5. si, 6. si, 7. ok mvc entonces
Your plan's baseline quota will refresh on 6/17/2026, 11:39:32 AM. You can upgrade to a Google AI Ultra plan to receive higher rate limits. .
Task
DIAGNOSTICO_INICIAL.mdRolController.javaEstadoController.javaUserController.javaLibroController.javaEstanteController.javaHistorialController.javaDetalleController.javaReservaLibroController.javaResenaLibroController.java
2:24 PM
