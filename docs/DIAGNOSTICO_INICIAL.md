# Diagnóstico Inicial de Microservicios

**Fecha**: 2026-06-12  
**Rama**: `feature/documentacion-tests-swagger-gateway-docker`  
**Java**: 17.0.17 (Oracle)  
**Maven**: 3.9.15 (wrapper)  
**Spring Boot**: 4.0.7  
**Spring Cloud**: 2025.1.1  

## Cambios previos al diagnóstico

- Se renombraron carpetas: `Rol (4)` → `Rol`, `Estado (1)` → `Estado`
- Se cambió `java.version` de 21 a 17 en todos los `pom.xml` (JDK 17 instalado)
- Se corrigió `modelVersion` en Multas de `4.0.7` a `4.0.0`

## Tabla de Diagnóstico

| Microservicio | Puerto | Compila | Tests actuales | Cantidad tests | Swagger | JaCoCo | Problemas detectados | Observaciones |
|---|---|---|---|---|---|---|---|---|
| Rol | 8081 | ✅ Sí | ❌ Solo ApplicationTests | 0 reales | ❌ No | ❌ No | ApplicationTests requiere MySQL | Sin Feign, CRUD simple |
| User | 8082 | ✅ Sí | ❌ Solo ApplicationTests | 0 reales | ❌ No | ❌ No | ApplicationTests requiere MySQL | Feign: RolClient. Login con PasswordEncoder |
| Catalogo | 8083 | ✅ Sí | ✅ CatalogoServiceTest | 7 | ✅ Sí (3.0.3) | ❌ No | Tiene OpenApiConfig | Mejor estado del proyecto |
| Estado | 8084 | ✅ Sí | ❌ Solo ApplicationTests | 0 reales | ❌ No | ❌ No | ApplicationTests requiere MySQL | Sin Feign, CRUD simple |
| Libro | 8085 | ✅ Sí | ❌ Solo ApplicationTests | 0 reales | ❌ No | ❌ No | ApplicationTests requiere MySQL | Feign: Catalogo, Estado, User |
| Estante | 8086 | ✅ Sí | ⚠️ EstanteRepositoryTest | ~1 | ✅ Sí (3.0.3) | ❌ No | Controller, DTOs y config duplicados en src/test | Feign: LibroClient |
| Historial | 8087 | ✅ Sí | ❌ Solo ApplicationTests | 0 reales | ❌ No | ❌ No | ApplicationTests requiere MySQL | Feign: Libro, User, Estado |
| Multas | 8088 | ✅ Sí | ⚠️ MultaServiceTest | ~pocos | ✅ Sí (3.0.3) | ❌ No | Controller, DTOs y config duplicados en src/test. modelVersion era 4.0.7 | Feign: User, Historial. Lógica compleja |
| ReservaLibro | 8089 | ✅ Sí | ❌ Solo ApplicationTests | 0 reales | ❌ No | ❌ No | ApplicationTests requiere MySQL | Feign: Libro, User, Multas. Lógica de bloqueo |
| Detalle | 8090 | ✅ Sí | ❌ Solo ApplicationTests | 0 reales | ❌ No | ❌ No | ApplicationTests requiere MySQL | Feign: Historial, Libro |
| ReseñaLibro | 8091 | ✅ Sí | ❌ Solo ApplicationTests | 0 reales | ❌ No | ❌ No | ApplicationTests requiere MySQL | Feign: Libro, User |

## Resumen

- **11/11** microservicios compilan correctamente ✅
- **3/11** tienen dependencia Swagger (Catalogo, Estante, Multas)
- **2/11** tienen tests de service reales (Catalogo, Multas)
- **0/11** tienen tests de controller
- **0/11** tienen JaCoCo configurado
- **0/11** tienen application-test.properties con H2
- **2/11** tienen archivos duplicados incorrectamente en src/test (Estante, Multas)
- **Todos** los ApplicationTests fallarán sin MySQL activo

## Archivos mal ubicados (a limpiar)

### Estante
- `src/test/java/.../config/OpenApiConfig.java` → mover a src/main
- `src/test/java/.../controller/EstanteController.java` → eliminar (duplicado)
- `src/test/java/.../dto/EstanteRequestDTO.java` → eliminar
- `src/test/java/.../dto/EstanteResponseDTO.java` → eliminar
- `src/test/java/.../dto/LibroResponseDTO.java` → eliminar

### Multas
- `src/test/java/.../config/OpenApiConfig.java` → mover a src/main
- `src/test/java/.../controller/MultaController.java` → eliminar (duplicado)
- `src/test/java/.../dto/EstadoMultasDTO.java` → eliminar
- `src/test/java/.../dto/HistorialResponseDTO.java` → eliminar
- `src/test/java/.../dto/MultaRequestDTO.java` → eliminar
- `src/test/java/.../dto/MultaResponseDTO.java` → eliminar
- `src/test/java/.../dto/UserResponseDTO.java` → eliminar
