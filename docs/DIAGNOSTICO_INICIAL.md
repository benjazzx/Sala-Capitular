# Diagnóstico Inicial — Sala Capitular

Fecha: 2026-06-12
Rama: `feature/documentacion-tests-swagger-gateway-docker`

## 1. Entorno

| Herramienta | Versión requerida | Estado |
|---|---|---|
| Java | 17 | Confirmado |
| Maven | Wrapper incluido (`mvnw.cmd` / `./mvnw`) | Confirmado |
| MySQL (XAMPP/phpMyAdmin) | 8.x | Disponible |
| Docker Desktop | última | Disponible |

## 2. Inventario de microservicios

El proyecto está compuesto por 11 microservicios Spring Boot independientes, cada uno con su propia base de datos MySQL y puerto dedicado.

| # | Servicio | Puerto | Base de datos | Dependencias (Feign) |
|---|---|---|---|---|
| 1 | Rol | 8081 | db_rol | — |
| 2 | User | 8082 | db_user | Rol |
| 3 | Catalogo | 8083 | db_catalogo | — |
| 4 | Estado | 8084 | db_estado | — |
| 5 | Libro | 8085 | db_libro | Catalogo, Estado, User |
| 6 | Estante | 8086 | db_estante | Libro |
| 7 | Historial | 8087 | db_historial | Libro, User, Estado |
| 8 | Multas | 8088 | db_multas | User, Historial |
| 9 | ReservaLibro | 8089 | db_reserva_libro | Libro, User, Multas |
| 10 | Detalle | 8090 | db_detalle | Historial, Libro |
| 11 | ReseñaLibro | 8091 | db_resena_libro | Libro, User |

## 3. Orden de arranque recomendado

Por las dependencias Feign, los servicios deben arrancarse respetando este orden:

1. **Sin dependencias (primero):** Rol, Catalogo, Estado
2. **Segundo nivel:** User (→ Rol)
3. **Tercer nivel:** Libro (→ Catalogo, Estado, User)
4. **Cuarto nivel:** Estante (→ Libro), Historial (→ Libro, User, Estado)
5. **Quinto nivel:** Multas (→ User, Historial), Detalle (→ Historial, Libro), ReseñaLibro (→ Libro, User)
6. **Último:** ReservaLibro (→ Libro, User, Multas)

## 4. Problemas detectados

### 4.1 Carpetas con nombres problemáticos
- `Rol (4)` → renombrada a `Rol`
- `Estado (1)` → renombrada a `Estado`

Los espacios y paréntesis complican los scripts de shell, los Dockerfiles y las rutas de compilación.

### 4.2 Bug en pom.xml de Multas
El `pom.xml` de Multas declaraba `<modelVersion>4.0.7</modelVersion>` cuando el valor correcto del esquema POM es `4.0.0`. Corregido. (El `4.0.7` parece haberse copiado por error desde la versión de Spring Boot.)

### 4.3 Archivos espurios en src/test
- **Estante:** `src/test/.../config/OpenApiConfig.java`, `controller/EstanteController.java`, y 3 DTOs eran copias del código de producción, no tests. Eliminados.
- **Multas:** `src/test/.../config/OpenApiConfig.java`, `controller/MultaController.java`, y 5 DTOs en la misma situación. Eliminados.

### 4.4 Tests vacíos
- `MultaServiceTest` y `EstanteRepositoryTest` existían como clases vacías sin pruebas. Reemplazados por suites completas.

### 4.5 Swagger incompleto
Solo Catalogo, Estante y Multas tenían la dependencia springdoc. Faltaba en los 8 restantes.

## 5. Compilación

Cada microservicio se compila de forma independiente desde su carpeta con:

```
.\mvnw.cmd clean compile -DskipTests
```

En Linux/macOS:

```
./mvnw clean compile -DskipTests
```

> Nota: el primer build descarga Spring Boot 4.0.7 y Spring Cloud 2025.1.1 desde Maven Central, por lo que requiere conexión a internet.

## 6. Estado tras esta fase

- Carpetas renombradas y limpias.
- pom.xml corregido y normalizado.
- Base lista para añadir Swagger, JaCoCo y la batería de tests.
