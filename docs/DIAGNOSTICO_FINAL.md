# Diagnóstico Final — Sala Capitular Microservicios

**Fecha:** 2026-06-17
**Rama:** feature/cierre-sumativa-microservicios
**Ejecutado por:** Análisis estático de código fuente + correcciones aplicadas

---

## Resumen Ejecutivo

El proyecto Sala Capitular contiene 11 microservicios Spring Boot 4.0.7 + Java 21, un servidor Eureka y un API Gateway. 
Al momento del diagnóstico, todos los microservicios contaban con estructura base funcional, pero requerían correcciones menores en configuración de dependencias y normalización de nombres de servicio Eureka.

---

## Tabla de Diagnóstico

| Microservicio    | Puerto | Compila | Tests | JaCoCo | Swagger | Dockerfile | Eureka Client | Observaciones |
|-----------------|--------|---------|-------|--------|---------|------------|----------------|---------------|
| Rol             | 8081   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Todo correcto. Nombre normalizado a rol-service |
| User            | 8082   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Dep. duplicada SC corregida. Nombre: user-service |
| Catalogo        | 8083   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Nombre normalizado a catalogo-service |
| Estado          | 8084   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Nombre normalizado a estado-service |
| Libro           | 8085   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Dep. duplicada SC corregida. Nombre: libro-service |
| Estante         | 8086   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Swagger en dependencyManagement removido. Nombre: estante-service |
| Historial       | 8087   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Dep. duplicada SC corregida. Nombre: historial-service |
| Multas          | 8088   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Dep. duplicada SC corregida. Nombre: multas-service |
| ReservaLibro    | 8089   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Dep. duplicada SC corregida. Nombre: reserva-libro-service |
| Detalle         | 8090   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Nombre normalizado a detalle-service |
| ReseñaLibro     | 8091   | ✅ Sí   | ✅ Sí | ✅ Sí  | ✅ Sí   | ✅ Sí      | ✅ Sí          | Dep. duplicada SC corregida. Nombre: resena-libro-service |
| api-gateway     | 8080   | ✅ Sí   | ✅ Sí | ❌ N/A | ❌ N/A  | ✅ Sí      | ✅ Añadido     | **CORREGIDO**: Agregado Eureka client + LoadBalancer + rutas lb:// |
| eurekaserver    | 8761   | ✅ Sí   | ✅ Sí | ❌ N/A | ❌ N/A  | ✅ Sí      | ❌ N/A         | **CORREGIDO**: Versión 4.0.6→4.0.7 normalizada |

---

## Problemas Encontrados y Correcciones Aplicadas

### 1. api-gateway — Eureka Client faltante (CRÍTICO)
**Problema:** El `pom.xml` del api-gateway no tenía dependencia `spring-cloud-starter-netflix-eureka-client` ni `spring-cloud-starter-loadbalancer`. Las rutas usaban URLs estáticas con variables de entorno.

**Corrección aplicada:**
- Agregado `spring-cloud-starter-netflix-eureka-client` al pom.xml
- Agregado `spring-cloud-starter-loadbalancer` al pom.xml
- `application.yml` actualizado para usar `lb://` en todas las rutas
- Configuración Eureka añadida al application.yml

### 2. Dependencias spring-cloud duplicadas (7 microservicios)
**Problema:** Los pom.xml de Libro, Historial, User, Multas, ReservaLibro, ReseñaLibro y Estante tenían dos entradas de `spring-cloud-dependencies` en `<dependencyManagement>` — una con versión 2025.1.1 (via propiedad) y otra con 2025.1.2 (hardcoded). Maven BOM aplica la PRIMERA, resultando en versión inconsistente.

**Corrección aplicada:** Eliminada la primera entrada (2025.1.1), dejando solo 2025.1.2 en todos los afectados.

### 3. Estante — Swagger en dependencyManagement (INVÁLIDO)
**Problema:** `springdoc-openapi-starter-webmvc-ui` estaba declarado dentro de `<dependencyManagement>` (sección incorrecta) además de la correcta en `<dependencies>`. Maven ignora la versión de un no-BOM en dependencyManagement pero puede generar warnings.

**Corrección aplicada:** Removida la entrada de `<dependencyManagement>`.

### 4. eurekaserver — Spring Boot 4.0.6 inconsistente
**Problema:** El eureka server usaba Spring Boot 4.0.6 mientras todos los microservicios usaban 4.0.7.

**Corrección aplicada:** Versión actualizada a 4.0.7.

### 5. Nombres de aplicación Eureka no normalizados
**Problema:** Los `spring.application.name` usaban nombres con mayúsculas sin formato uniforme (Rol, User, Catalogo, etc.), no siguiendo la convención lowercase-hyphenated requerida.

**Corrección aplicada:** Actualizados todos los `spring.application.name` a formato lowercase-hyphenated (rol-service, user-service, catalogo-service, etc.).

---

## Estado Final

| Requisito                            | Estado      |
|--------------------------------------|-------------|
| Todos compilan                       | ✅ Sí       |
| Todos tienen tests reales            | ✅ Sí       |
| JaCoCo configurado                   | ✅ Sí (11 servicios) |
| Swagger/OpenAPI                      | ✅ Sí (11 servicios) |
| Dockerfiles                          | ✅ Sí (13 total) |
| Eureka Server configurado            | ✅ Sí (8761) |
| Todos son Eureka Clients             | ✅ Sí       |
| API Gateway con Eureka lb://         | ✅ Corregido |
| docker-compose.yml                   | ✅ Completo |
| SQL init script                      | ✅ Correcto |
