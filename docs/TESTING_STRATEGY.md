# Estrategia de Testing

## Objetivo

Establecer una base de pruebas automatizadas (unitarias y de integración ligera con MockMvc)
para los 11 microservicios del sistema **Sala Capitular**, con medición de cobertura mediante
JaCoCo y un mínimo de **90% de cobertura de instrucciones** por módulo.

## Perfil de test (H2)

Cada microservicio incluye `src/test/resources/application-test.properties` con una base de
datos en memoria H2, evitando la dependencia de MySQL/XAMPP para ejecutar los tests:

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.cloud.openfeign.lazy-attributes-resolution=true
```

La dependencia `com.h2database:h2` (scope `test`) se agregó a todos los `pom.xml`.

Los `*ApplicationTests` (smoke test de carga de contexto generado por Spring Initializr) ahora
usan `@ActiveProfiles("test")` para apuntar a H2 en lugar de MySQL:

```java
@SpringBootTest
@ActiveProfiles("test")
class XApplicationTests {
    @Test
    void contextLoads() { }
}
```

## Niveles de prueba

### 1. Tests de Service (Fase 4)

Pruebas unitarias con JUnit 5 + Mockito sobre la capa `service`, mockeando repositorios
(`@Mock JpaRepository`) y, cuando aplica, clientes Feign (`@Mock XClient`). Cubren:

- Casos de éxito de cada operación CRUD.
- Casos de error (entidad no encontrada → excepción de negocio).
- Reglas de negocio específicas (validaciones, cálculos, estados).

### 2. Tests de Controller (Fase 5)

Pruebas con `@WebMvcTest` + `MockMvc`, mockeando la capa `service` (`@MockBean`). Cubren:

- Códigos de estado HTTP esperados (200, 201, 204, 404, 400, etc.).
- Serialización/deserialización de DTOs (JSON).
- Validaciones de entrada (`@Valid` en request DTOs).

### 3. Tests de Excepciones (Fase 6)

El `GlobalExceptionHandler` de cada microservicio se ejercita desde los tests de controller,
forzando que el service mockeado lance las excepciones de negocio (`ResourceNotFoundException`,
`IllegalArgumentException`, errores de validación, etc.) y verificando el cuerpo/estado de la
respuesta de error.

## JaCoCo

Se agregó `jacoco-maven-plugin` (versión `0.8.13`) a los 11 `pom.xml`, con:

- `prepare-agent`: instrumenta las clases antes de ejecutar los tests.
- `report` (fase `verify`): genera el reporte HTML/XML en `target/site/jacoco`.
- `check` (fase `verify`): falla el build si la cobertura de instrucciones del bundle es
  menor al **90%**.

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.13</version>
    <executions>
        <execution>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>verify</phase>
            <goals><goal>report</goal></goals>
        </execution>
        <execution>
            <id>check</id>
            <phase>verify</phase>
            <goals><goal>check</goal></goals>
            <configuration>
                <rules>
                    <rule>
                        <element>BUNDLE</element>
                        <limits>
                            <limit>
                                <counter>INSTRUCTION</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.90</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Ejecución

```bash
mvn clean verify
```

Genera el reporte de cobertura en `target/site/jacoco/index.html` por microservicio.
El resultado consolidado se documenta en `docs/COVERAGE_REPORT.md` (Fase 7).

## Limpieza de archivos duplicados

Se eliminaron archivos duplicados (controllers, DTOs y `OpenApiConfig`) que estaban
incorrectamente ubicados en `src/test/java` de **Estante** y **Multas** (copias del código de
`src/main`, sin valor como tests).
