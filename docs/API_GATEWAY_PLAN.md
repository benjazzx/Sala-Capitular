# API Gateway Plan — Sala Capitular

## Decisión: Spring Cloud Gateway MVC (no WebFlux)

Se eligió la variante **MVC** (`spring-cloud-starter-gateway-server-webmvc`) en lugar de WebFlux por consistencia: los 11 microservicios usan el stack servlet/MVC (`spring-boot-starter-webmvc`), de modo que mantener un modelo de programación bloqueante uniforme reduce la carga cognitiva del equipo y evita mezclar paradigmas reactivos con no reactivos.

## Responsabilidades del gateway

1. **Punto de entrada único** en el puerto `8080`.
2. **Enrutamiento por path** hacia cada microservicio.
3. **Configurabilidad** de los destinos vía variables de entorno (`MS_*_URL`), lo que permite el mismo binario en local y en Docker.

## Tabla de rutas

| Path | Servicio destino | Puerto interno |
|---|---|---|
| `/api/roles/**` | rol | 8081 |
| `/api/users/**` | user | 8082 |
| `/api/catalogos/**` | catalogo | 8083 |
| `/api/estados/**` | estado | 8084 |
| `/api/libros/**` | libro | 8085 |
| `/api/estantes/**` | estante | 8086 |
| `/api/historiales/**` | historial | 8087 |
| `/api/multas/**` | multas | 8088 |
| `/api/reservas/**` | reserva | 8089 |
| `/api/detalles/**` | detalle | 8090 |
| `/api/resenas/**` | resena | 8091 |

## Configuración (resumen)

Las rutas se definen en `api-gateway/src/main/resources/application.yml` bajo `spring.cloud.gateway.mvc.routes`. Cada destino usa `${MS_X_URL:http://localhost:PUERTO}` para tener un valor por defecto local y permitir override en Docker.

## Futuras mejoras (fuera de alcance actual)

- **Autenticación centralizada** (JWT) validada en el gateway antes de reenviar.
- **Rate limiting** por cliente.
- **Filtros de logging/trazabilidad** (correlation IDs).
- **Resiliencia** (Resilience4j circuit breaker) para degradar con elegancia cuando un servicio cae.
- **Service discovery** (Eureka/Consul) para eliminar las URLs estáticas.
