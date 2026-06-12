# Coverage Report — Sala Capitular

> Este reporte se genera localmente. Tras ejecutar `mvn clean verify` en cada
> microservicio, el HTML de JaCoCo queda en `target/site/jacoco/index.html`.
> Copia aquí los porcentajes reales una vez ejecutado.

## Cómo generar

Por servicio:
```
mvn clean verify
# luego abrir target/site/jacoco/index.html
```

## Resumen (rellenar con valores reales)

| Servicio | Instrucciones | Ramas | Líneas | Métodos |
|---|---|---|---|---|
| Rol | _%_ | _%_ | _%_ | _%_ |
| User | _%_ | _%_ | _%_ | _%_ |
| Catalogo | _%_ | _%_ | _%_ | _%_ |
| Estado | _%_ | _%_ | _%_ | _%_ |
| Libro | _%_ | _%_ | _%_ | _%_ |
| Estante | _%_ | _%_ | _%_ | _%_ |
| Historial | _%_ | _%_ | _%_ | _%_ |
| Multas | _%_ | _%_ | _%_ | _%_ |
| ReservaLibro | _%_ | _%_ | _%_ | _%_ |
| Detalle | _%_ | _%_ | _%_ | _%_ |
| ReseñaLibro | _%_ | _%_ | _%_ | _%_ |

## Cobertura esperada

Las suites cubren explícitamente, por servicio:
- Capa **service**: todos los métodos públicos (CRUD + reglas de negocio + ramas de error).
- Capa **controller**: todos los endpoints, códigos 200/201/204/404 y 400 (validación y excepciones de negocio).

Las clases de **config** (OpenApiConfig, PasswordConfig) y **client** (interfaces Feign) no se testean directamente; pueden excluirse del cómputo de JaCoCo si se desea un porcentaje más representativo de la lógica.
