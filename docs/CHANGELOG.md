# Changelog — Sala Capitular

Todos los cambios relevantes de la rama `feature/documentacion-tests-swagger-gateway-docker`.

## [Unreleased]

### Added
- Documentación de proyecto en `docs/`: DIAGNOSTICO_INICIAL, API_DOCUMENTATION, TESTING_STRATEGY, ARCHITECTURE, DOCKER_GUIDE, API_GATEWAY_PLAN, ROADMAP, COVERAGE_REPORT, CHANGELOG.
- Dependencia Swagger/OpenAPI (springdoc) en los 8 servicios que no la tenían (Rol, User, Estado, Libro, Historial, ReservaLibro, Detalle, ReseñaLibro).
- Clases `OpenApiConfig` en todos los servicios.
- Plugin `jacoco-maven-plugin` en los 11 pom.xml.
- Dependencia H2 (scope test) y `application-test.properties` con perfil `test` en los 11 servicios.
- Suite completa de tests de servicio (11) y de controlador (11) con JUnit 5, Mockito, AssertJ y MockMvc.
- Módulo `api-gateway` (Spring Cloud Gateway MVC) con enrutamiento por path a los 11 servicios.
- Dockerfiles multi-stage por servicio + gateway, `docker-compose.yml`, `.env.example` y script de inicialización de las 11 bases de datos.

### Changed
- Carpetas `Rol (4)` → `Rol` y `Estado (1)` → `Estado`.
- `application.properties` de cada servicio parametrizado con `${VAR:default}` para datasource y URLs Feign (compatible con local y Docker).
- `*ApplicationTests` anotados con `@ActiveProfiles("test")`.

### Fixed
- `pom.xml` de Multas: `<modelVersion>` corregido de `4.0.7` a `4.0.0`.

### Removed
- Archivos espurios en `src/test` de Estante (OpenApiConfig, EstanteController y 3 DTOs duplicados).
- Archivos espurios en `src/test` de Multas (OpenApiConfig, MultaController y 5 DTOs duplicados).
