# Coverage Report - Sala Capitular

Fecha de validacion: 2026-06-12  
Comando usado por servicio en Windows:

```powershell
.\mvnw.cmd clean verify
```

En Linux/macOS:

```bash
./mvnw clean verify
```

Cada ejecucion genera el HTML de JaCoCo en `target/site/jacoco/index.html` y el resumen CSV en `target/site/jacoco/jacoco.csv`.

## Resumen JaCoCo

| Servicio | Instrucciones | Ramas | Lineas | Metodos |
|---|---:|---:|---:|---:|
| Rol | 98.39% | 83.33% | 96.55% | 95.45% |
| User | 96.12% | 91.67% | 94.29% | 92.86% |
| Catalogo | 98.57% | 83.33% | 96.61% | 95.45% |
| Estado | 91.56% | 83.33% | 89.66% | 86.36% |
| Libro | 94.25% | 91.67% | 90.68% | 96.30% |
| Estante | 90.47% | 83.33% | 88.16% | 82.61% |
| Historial | 92.57% | 75.00% | 93.33% | 82.61% |
| Multas | 93.06% | 88.89% | 91.54% | 85.29% |
| ReservaLibro | 90.61% | 83.33% | 88.00% | 92.59% |
| Detalle | 92.88% | 75.00% | 92.59% | 87.50% |
| ResenaLibro | 94.26% | 75.00% | 93.26% | 88.46% |

## Resultado

Los 11 microservicios superan el umbral configurado de JaCoCo: 90% de cobertura de instrucciones (`INSTRUCTION COVEREDRATIO`).

## Alcance de las pruebas

Las suites cubren explicitamente:

- Capa `service`: CRUD, reglas de negocio, validaciones cruzadas via Feign y ramas de error.
- Capa `controller`: endpoints principales con MockMvc, validaciones `@Valid`, respuestas 200/201/204/400/404 y manejo de excepciones.
- Contexto Spring Boot: `contextLoads()` con perfil `test` y base H2 en memoria.

Las clases de configuracion, clientes Feign, DTOs y modelos no concentran logica de negocio; se validan indirectamente por compilacion, tests de contexto y pruebas de controlador/servicio.
