# Testing Strategy — Sala Capitular

## Objetivo

Cubrir cada microservicio con pruebas unitarias de la capa de servicio (lógica de negocio) y de la capa de controlador (contratos HTTP), midiendo la cobertura con JaCoCo.

## Tipos de prueba

### 1. Service tests (lógica de negocio)
- **Framework:** JUnit 5 + Mockito + AssertJ
- **Anotación:** `@ExtendWith(MockitoExtension.class)`
- Se mockean el repositorio y todos los clientes Feign con `@Mock`; el servicio se inyecta con `@InjectMocks`.
- Cubren: listar, obtener por ID (presente y ausente), crear, actualizar (existe y no existe), eliminar (existe y no existe) y las reglas de negocio específicas (validaciones cruzadas vía Feign, ISBN único, bloqueo por multas, roles ADMIN/AUTOR, etc.).

### 2. Controller tests (capa web)
- **Framework:** `@WebMvcTest` + MockMvc + Mockito
- El servicio se mockea con `@MockitoBean`, compatible con Spring Boot 4.
- Cubren: cada endpoint, los códigos 200/201/204/404, las validaciones de DTO que producen 400, y la traducción de `RuntimeException` a 400 vía `GlobalExceptionHandler`.

### 3. Application context test
- Cada servicio mantiene su `*ApplicationTests.contextLoads()` anotado con `@ActiveProfiles("test")` para que el contexto cargue contra H2 en memoria en lugar de MySQL.

## Perfil de test (H2)

Cada servicio tiene `src/test/resources/application-test.properties` apuntando a una base H2 en memoria:

```
spring.datasource.url=jdbc:h2:mem:db_xxx;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

Esto evita depender de MySQL/XAMPP para correr la suite.

## Cobertura de excepciones

El `GlobalExceptionHandler` de cada servicio se ejercita indirectamente desde los controller tests: cuando el servicio mockeado lanza `RuntimeException`, MockMvc verifica que el handler responde 400, y las validaciones `@Valid` verifican el manejo de `MethodArgumentNotValidException`.

## Inventario de suites

| Servicio | ServiceTest | ControllerTest |
|---|---|---|
| Rol | ✓ | ✓ |
| User | ✓ | ✓ |
| Catalogo | ✓ (preexistente) | ✓ |
| Estado | ✓ | ✓ |
| Libro | ✓ | ✓ |
| Estante | ✓ | ✓ |
| Historial | ✓ | ✓ |
| Multas | ✓ | ✓ |
| ReservaLibro | ✓ | ✓ |
| Detalle | ✓ | ✓ |
| ReseñaLibro | ✓ | ✓ |

## Ejecución

Por servicio:
```
.\mvnw.cmd clean verify
```

`verify` ejecuta los tests y genera el reporte JaCoCo en `target/site/jacoco/index.html`.

En Linux/macOS usa `./mvnw clean verify`.

## Convenciones

- Nombres de test: `metodo_condicion_resultadoEsperado` (ej. `guardar_isbnDuplicado_debeLanzarExcepcion`).
- Patrón Arrange-Act-Assert.
- Un assert principal por comportamiento; `verify(...)` para confirmar interacción con mocks.
