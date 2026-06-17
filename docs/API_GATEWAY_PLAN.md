# API Gateway Plan — Sala Capitular

## Decisión: Spring Cloud Gateway MVC (no WebFlux)

Se eligió la variante **MVC** (`spring-cloud-starter-gateway-server-webmvc`) en lugar de WebFlux por consistencia: los 11 microservicios usan el stack servlet/MVC (`spring-boot-starter-webmvc`), de modo que mantener un modelo de programación bloqueante uniforme reduce la carga cognitiva del equipo y evita mezclar paradigmas reactivos con no reactivos.

## Responsabilidades del gateway

1. **Punto de entrada único** en el puerto `8080`.
2. **Enrutamiento por path** hacia cada microservicio usando `lb://` URIs con Eureka Service Discovery.
3. **Service Discovery:** usa `spring-cloud-starter-netflix-eureka-client` + `spring-cloud-starter-loadbalancer` para resolver `lb://rol-service` → instancia real del servicio registrado en Eureka.
4. **Configurabilidad** vía variables de entorno (`EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`) para apuntar al Eureka Server correcto en cada entorno.

## Tabla de rutas

| Path | URI lb:// | Eureka Name | Puerto directo |
|---|---|---|---|
| `/api/roles/**` | `lb://rol-service` | rol-service | 8081 |
| `/api/users/**` | `lb://user-service` | user-service | 8082 |
| `/api/catalogos/**` | `lb://catalogo-service` | catalogo-service | 8083 |
| `/api/estados/**` | `lb://estado-service` | estado-service | 8084 |
| `/api/libros/**` | `lb://libro-service` | libro-service | 8085 |
| `/api/estantes/**` | `lb://estante-service` | estante-service | 8086 |
| `/api/historiales/**` | `lb://historial-service` | historial-service | 8087 |
| `/api/multas/**` | `lb://multas-service` | multas-service | 8088 |
| `/api/reservas/**` | `lb://reserva-libro-service` | reserva-libro-service | 8089 |
| `/api/detalles/**` | `lb://detalle-service` | detalle-service | 8090 |
| `/api/resenas/**` | `lb://resena-libro-service` | resena-libro-service | 8091 |

## Configuración (resumen)

Las rutas se definen en `api-gateway/src/main/resources/application.yml` bajo `spring.cloud.gateway.mvc.routes`. Cada destino usa `lb://NOMBRE-SERVICIO` para service discovery vía Eureka. Los accesos directos por puerto también siguen funcionando.

**Prerrequisito:** Eureka Server debe estar levantado en `:8761` antes de iniciar el gateway.

## Futuras mejoras (fuera de alcance actual)

- **Autenticación centralizada** (JWT) validada en el gateway antes de reenviar.
- **Rate limiting** por cliente.
- **Filtros de logging/trazabilidad** (correlation IDs).
- **Resiliencia** (Resilience4j circuit breaker) para degradar con elegancia cuando un servicio cae.
