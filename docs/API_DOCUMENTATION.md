# Documentación de API (Swagger / OpenAPI)

**Versión springdoc-openapi**: `2.x` (springdoc-openapi-starter-webmvc-ui 3.0.3, compatible con Spring Boot 4)

Todos los microservicios del sistema **Sala Capitular** exponen su documentación interactiva
mediante Swagger UI gracias a `springdoc-openapi-starter-webmvc-ui`.

## Acceso a Swagger UI

Cada microservicio expone su documentación en:

```
http://localhost:<puerto>/swagger-ui.html
```

Y el contrato OpenAPI en formato JSON en:

```
http://localhost:<puerto>/v3/api-docs
```

## Microservicios y puertos

| Microservicio | Puerto | Swagger UI | Tag principal |
|---|---|---|---|
| Rol | 8081 | http://localhost:8081/swagger-ui.html | Roles |
| User | 8082 | http://localhost:8082/swagger-ui.html | Usuarios |
| Catalogo | 8083 | http://localhost:8083/swagger-ui.html | Catálogo |
| Estado | 8084 | http://localhost:8084/swagger-ui.html | Estados |
| Libro | 8085 | http://localhost:8085/swagger-ui.html | Libros |
| Estante | 8086 | http://localhost:8086/swagger-ui.html | Estantes |
| Historial | 8087 | http://localhost:8087/swagger-ui.html | Historial |
| Multas | 8088 | http://localhost:8088/swagger-ui.html | Multas |
| ReservaLibro | 8089 | http://localhost:8089/swagger-ui.html | Reservas |
| Detalle | 8090 | http://localhost:8090/swagger-ui.html | Detalle |
| ReseñaLibro | 8091 | http://localhost:8091/swagger-ui.html | Reseñas |

## Configuración

Cada microservicio incluye una clase `OpenApiConfig` (paquete `config`) que define metadatos
básicos del API mediante un bean `OpenAPI`:

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio <Nombre> - Sala Capitular")
                        .version("1.0.0")
                        .description("..."));
    }
}
```

## Anotaciones utilizadas en los controllers

- `@Tag(name = "...", description = "...")`: agrupa los endpoints de un controller en Swagger UI.
- `@Operation(summary = "...", description = "...")`: documenta cada endpoint (qué hace, parámetros relevantes).

Ejemplo (`RolController`):

```java
@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Operaciones CRUD para la gestión de roles")
public class RolController {

    @GetMapping
    @Operation(summary = "Listar todos los roles", description = "Retorna una lista con todos los roles registrados en el sistema.")
    public List<RolResponseDTO> obtenerTodos() { ... }
}
```

## Dependencia Maven agregada

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.0.3</version>
</dependency>
```

Esta dependencia ya estaba presente en Catalogo, Estante y Multas, y se agregó en el resto:
Rol, User, Estado, Libro, Historial, ReservaLibro, Detalle y ReseñaLibro.

## Endpoints documentados

Todos los endpoints CRUD (`GET`, `POST`, `PUT`/`PATCH`, `DELETE`) y endpoints de negocio
específicos (login, validaciones, endpoints por relación entre entidades, etc.) cuentan con
`@Operation` describiendo su propósito.

Para el detalle completo de cada endpoint, parámetros y esquemas de request/response, consultar
Swagger UI de cada microservicio levantado localmente — el contrato se genera automáticamente
a partir de los DTOs y anotaciones de validación (`@NotNull`, `@NotBlank`, `@Email`, etc.).
