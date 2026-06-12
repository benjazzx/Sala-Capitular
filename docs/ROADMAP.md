# Roadmap y Recomendaciones — Sala Capitular

Este documento recoge mejoras recomendadas tras la fase de documentación, tests, Swagger, gateway y Docker. No son cambios incluidos en esta rama; son propuestas priorizadas.

## Prioridad alta

1. **Autenticación y autorización con JWT.**
   Hoy el login devuelve los datos del usuario pero no emite un token. Recomendación: emitir JWT en `/api/users/login`, validar en el API Gateway y propagar la identidad a los servicios.

2. **Resiliencia en llamadas Feign.**
   Las validaciones cruzadas son síncronas y sin tolerancia a fallos. Añadir Resilience4j (circuit breaker, timeouts, retries) evitaría fallos en cascada cuando un servicio dependiente cae.

3. **Migraciones de base de datos versionadas.**
   `ddl-auto=update` es cómodo en desarrollo pero arriesgado. Migrar a Flyway o Liquibase para tener un esquema reproducible y versionado.

## Prioridad media

4. **Service discovery.**
   Sustituir las URLs estáticas (`ms.*.url`) por un registro (Eureka/Consul) o por DNS de Kubernetes, eliminando configuración manual de hosts.

5. **Manejo de excepciones más rico.**
   El `GlobalExceptionHandler` traduce todo `RuntimeException` a 400. Conviene introducir excepciones de dominio (`NotFoundException` → 404, `ConflictException` → 409) para semántica HTTP correcta.

6. **Observabilidad.**
   Añadir Micrometer + Prometheus + Grafana, y trazabilidad distribuida (correlation IDs) para seguir una petición a través de los servicios.

7. **Tests de integración.**
   Complementar los unit tests con tests de integración usando Testcontainers (MySQL real) y, para los flujos cruzados, WireMock para simular los servicios Feign.

## Prioridad baja

8. **Caché de lecturas frecuentes** (ej. roles, estados, catálogos) para reducir llamadas Feign repetidas.

9. **Paginación** en los endpoints de listado (`GET /api/...`) que hoy devuelven colecciones completas.

10. **Internacionalización** de los mensajes de error (hoy fijos en español).

11. **Pipeline CI/CD** (GitHub Actions) que ejecute `mvn verify` por servicio y publique el reporte de cobertura.

## Notas de mantenimiento

- Confirmar el `spring-boot-starter-parent` 4.0.7 y `spring-cloud 2025.1.1` declarados en los pom.xml; verificar su disponibilidad real en Maven Central antes de un release.
- Mantener la convención de carpetas sin espacios ni paréntesis para no romper scripts ni Dockerfiles.
