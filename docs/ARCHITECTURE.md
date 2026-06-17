# Arquitectura — Sala Capitular

## Visión general

Sala Capitular es un sistema de gestión de biblioteca construido como un conjunto de 11 microservicios Spring Boot independientes. Cada servicio posee su propia base de datos MySQL (patrón *database-per-service*) y se comunica con los demás de forma síncrona mediante clientes Feign sobre HTTP. Un API Gateway (Spring Cloud Gateway MVC) actúa como punto de entrada único.

## Diagrama de dependencias

```
                         ┌──────────────┐
   Cliente HTTP  ───────▶│ API Gateway  │  :8080
                         └──────┬───────┘
        ┌───────────────────────┼────────────────────────────┐
        ▼                       ▼                             ▼
   ┌─────────┐            ┌──────────┐                  ┌──────────┐
   │  Rol    │◀───────────│  User    │                  │ Catalogo │
   │ :8081   │            │ :8082    │                  │ :8083    │
   └─────────┘            └────┬─────┘                  └────┬─────┘
                               │      ┌──────────┐           │
   ┌─────────┐                 │      │ Estado   │◀──────────┤
   │ Estado  │◀────────────────┤      │ :8084    │           │
   └─────────┘                 │      └────┬─────┘           │
                               ▼           │                 │
                          ┌──────────┐◀────┴─────────────────┘
                          │  Libro   │ :8085
                          └────┬─────┘
        ┌──────────┬──────────┼──────────┬────────────┐
        ▼          ▼          ▼          ▼            ▼
   ┌────────┐ ┌─────────┐ ┌────────┐ ┌─────────┐ ┌──────────┐
   │Estante │ │Historial│ │Detalle │ │ Reseña  │ │ Reserva  │
   │ :8086  │ │ :8087   │ │ :8090  │ │ :8091   │ │ :8089    │
   └────────┘ └────┬────┘ └────────┘ └─────────┘ └────┬─────┘
                   ▼                                   │
              ┌─────────┐                              │
              │ Multas  │◀─────────────────────────────┘
              │ :8088   │
              └─────────┘
```

## Componentes por servicio

Cada microservicio sigue una arquitectura en capas idéntica:

- **controller** — Endpoints REST (`@RestController`), validación con `@Valid`.
- **service** — Lógica de negocio, validaciones cruzadas vía Feign.
- **repository** — Acceso a datos (Spring Data JPA).
- **model** — Entidades JPA.
- **dto** — Objetos de transferencia (request/response).
- **client** — Interfaces Feign hacia otros servicios.
- **config** — `OpenApiConfig` (Swagger); en User además `PasswordConfig`.
- **exception** — `GlobalExceptionHandler` (`@RestControllerAdvice`).

## Reglas de negocio destacadas

- **User:** no se puede registrar un usuario con rol ADMIN vía API pública; las contraseñas se almacenan con BCrypt.
- **Libro:** el autor debe tener rol AUTOR; el ISBN es único.
- **Multas:** solo un usuario con rol ADMIN puede emitir multas.
- **ReservaLibro:** un usuario con más de 3 puntos de multa queda bloqueado; un libro no puede tener dos reservas ACTIVAS simultáneas.

## Comunicación

- **Síncrona, vía Feign.** Cada llamada cruzada valida la existencia del recurso remoto y traduce `FeignException.NotFound` a un error de negocio legible.
- **Descubrimiento de servicios vía Eureka:** Todos los microservicios se registran en el Eureka Server (`:8761`) con nombres lowercase-hyphenated (`rol-service`, `user-service`, etc.). El API Gateway resuelve los servicios usando `lb://` URIs (Spring Cloud LoadBalancer + Eureka).
- Las URLs inter-servicio de los Feign Clients (Historial→Libro, etc.) se parametrizan via `ms.*.url` como fallback local; en Docker Compose se pasan como variables de entorno.

## Persistencia

- 11 esquemas MySQL independientes.
- `ddl-auto=update` en producción local; `create-drop` con H2 en el perfil de test.

## Observabilidad

- Cada servicio expone Swagger UI y el contrato OpenAPI.
- El gateway expone los endpoints de Actuator `health`, `info` y `gateway`.

## Limitaciones conocidas / deuda técnica

Ver `docs/ROADMAP.md`. En resumen: comunicación puramente síncrona (sin resiliencia tipo circuit breaker), y autenticación/JWT no implementada a nivel de gateway. Service discovery ya está implementado con Eureka.
