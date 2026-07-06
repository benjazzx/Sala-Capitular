# Colección Postman — Sala Capitular

**Archivos:**
- `docs/postman/Sala-Capitular.postman_collection.json` — colección (Postman Collection v2.1)
- `docs/postman/Sala-Capitular.local_environment.json` — environment local

**Total:** 13 carpetas, 124 requests.

---

## 1. Cómo importar

### Colección
1. Abrir Postman.
2. **File → Import** (o el botón **Import** en la barra lateral).
3. Seleccionar `docs/postman/Sala-Capitular.postman_collection.json`.
4. Confirmar importación. Aparecerá "Sala Capitular - API Collection" con 13 carpetas.

### Environment
1. **File → Import** (mismo flujo) o el ícono de engranaje (Environments) → **Import**.
2. Seleccionar `docs/postman/Sala-Capitular.local_environment.json`.
3. En la esquina superior derecha de Postman, seleccionar el environment **"Sala Capitular - Local"** en el dropdown.

Sin el environment seleccionado, las variables `{{baseUrl}}`, `{{eurekaUrl}}` y los IDs no se resuelven y las requests fallarán.

---

## 2. Cómo se ejecuta

Toda la colección apunta al **API Gateway en `http://localhost:8080`**, no a los puertos individuales de cada microservicio. El Gateway resuelve internamente cada ruta hacia el microservicio correspondiente vía Eureka (`lb://`).

Antes de ejecutar la colección debe estar levantado, en este orden:

1. MySQL
2. Eureka Server (`:8761`)
3. Los 11 microservicios (deben registrarse en Eureka)
4. API Gateway (`:8080`)

Ver [`docs/DOCKER_GUIDE.md`](DOCKER_GUIDE.md) para levantar todo con `docker compose up --build`, o [`README.md`](../README.md) para arranque manual.

---

## 3. Orden recomendado de ejecución

La colección tiene **dependencias entre carpetas** por el diseño de las reglas de negocio del sistema (multas que bloquean reservas, libros usados como referencia en estantes/historiales/etc.). Ejecutar las carpetas **en el orden numérico** (00 → 12), ya sea manualmente o con el **Collection Runner** de Postman:

| Orden | Carpeta | Por qué este orden |
|-------|---------|---------------------|
| 1 | 00 - Health Check | Confirma que Eureka y el Gateway están arriba antes de probar nada más |
| 2 | 01 - Roles | Sin dependencias |
| 3 | 02 - Estados | Sin dependencias |
| 4 | 03 - Catalogos | Crea un catálogo nuevo y guarda `catalogoId`, usado luego por Libros |
| 5 | 04 - Users | Sin dependencias (usa IDs semilla fijos) |
| 6 | 05 - Libros | Crea un libro nuevo y guarda `libroId`, usado por Estantes, Historiales, Detalles y Resenas |
| 7 | 06 - Estantes | Usa `{{libroId}}` |
| 8 | 07 - Historiales | Crea un historial nuevo y guarda `historialId`, usado por Multas y Detalles |
| 9 | 08 - Multas | Crea una multa de "setup" contra el usuario semilla id 6 que lo deja bloqueado (>3 puntos). **Necesario antes de la carpeta 09** |
| 10 | 09 - Reservas | La prueba "usuario bloqueado por multas" depende de la multa creada en la carpeta 08 |
| 11 | 10 - Detalles | Usa `{{historialId}}` y `{{libroId}}` |
| 12 | 11 - Resenas | Usa `{{libroId}}` |
| 13 | 12 - Gateway y Errores | Pruebas de enrutamiento, ejecutar al final como verificación general |

Si se ejecuta fuera de orden, los escenarios de regla de negocio que dependen de estado creado por otra carpeta (notablemente 08 → 09) pueden no reproducirse exactamente, aunque el resto de la colección sigue siendo válido.

---

## 4. Qué carpetas validan CRUD

Las siguientes carpetas cubren el ciclo completo **GET listar / GET por id / POST crear / PUT actualizar**, más errores 400 y 404:

- 03 - Catalogos
- 04 - Users
- 05 - Libros
- 06 - Estantes
- 07 - Historiales
- 08 - Multas
- 09 - Reservas
- 10 - Detalles
- 11 - Resenas

**01 - Roles** y **02 - Estados** son una excepción deliberada: ambos microservicios validan el campo `nombre` contra un **enum cerrado** (`CLIENTE|ADMIN|AUTOR` y `DISPONIBLE|RESERVADO|PRESTADO|DAÑADO` respectivamente) que además es `unique` en base de datos, y los 3/4 valores posibles ya están cargados por el `DataInitializer`. No existe ninguna combinación de datos que produzca un alta exitosa sin violar esa regla de negocio (sería necesario eliminar primero un valor semilla, lo que rompería datos usados por otros microservicios). Por eso estas dos carpetas solo cubren GET listar, GET por id, PUT actualizar y los casos de error (duplicado, valor no permitido, recurso inexistente) — el endpoint POST y DELETE existen en el código (confirmados en `RolController` y `EstadoController`) pero no se ejercitan con una creación "feliz" en la colección.

DELETE de recursos reales tampoco se ejecuta en ninguna carpeta para no destruir datos semilla compartidos entre microservicios (por ejemplo, borrar un libro rompería Estantes, Historiales, Detalles y Resenas que lo referencian). En su lugar, cada carpeta incluye un **DELETE sobre un id inexistente (9999)**, que sí ejercita el endpoint DELETE real y su manejo de error (400).

---

## 5. Qué carpetas validan reglas de negocio

| Carpeta | Regla de negocio probada |
|---------|---------------------------|
| 01 - Roles | Unicidad de `nombre` + enum cerrado (CLIENTE/ADMIN/AUTOR) |
| 02 - Estados | Unicidad de `nombre` + enum cerrado (DISPONIBLE/RESERVADO/PRESTADO/DAÑADO) |
| 04 - Users | No se puede registrar un usuario con rol `ADMIN` vía API pública |
| 05 - Libros | El autor debe tener rol `AUTOR`; el ISBN es único |
| 08 - Multas | Solo un usuario con rol `ADMIN` puede emitir multas |
| 09 - Reservas | Un usuario con más de 3 puntos de multa queda bloqueado; un libro no puede tener dos reservas `ACTIVA` simultáneas |
| 11 - Resenas | La calificación debe estar entre 1 y 5 |

---

## 6. Qué requests validan errores 400, 404 y 409

Cada request de error tiene el test `pm.test("Error controlado esperado", ...)` que acepta `[400, 404, 409]`. En la práctica, según el código revisado (`GlobalExceptionHandler` de cada microservicio):

- **404 real** únicamente en `GET /{id}` con un id inexistente (el controller responde `ResponseEntity.notFound().build()`).
- **400** en todo lo demás: validaciones de `@Valid` (`MethodArgumentNotValidException`), reglas de negocio y `PUT`/`DELETE` sobre un id inexistente (el service lanza `RuntimeException`, capturada por el `@ExceptionHandler(RuntimeException.class)` que siempre devuelve 400, incluso para "no encontrado").

No se identificó ningún endpoint que devuelva 409 explícitamente en el código — se deja el código en el rango aceptado del test por si la pauta lo exige, pero la evidencia real observada es 400/404 únicamente. Esto se documenta para que no se interprete como una afirmación no verificada.

La carpeta **12 - Gateway y Errores** valida además que una ruta no configurada en el Gateway (`/api/ruta-inexistente`) responde con error en vez de colgarse o hacer fallback silencioso.

---

## 7. Qué evidencias tomar para la defensa

Para sustentar la entrega con evidencia real (no solo capturas de pantalla del código), se recomienda:

1. **Captura de la consola de Eureka** (`http://localhost:8761`) mostrando los 12 servicios registrados (11 microservicios + api-gateway).
2. **Captura del Collection Runner** de Postman al finalizar la corrida completa (00 → 12), mostrando el resumen de tests pasados/fallidos.
3. **Export del resultado del Runner** (botón "Export Results" en Postman) como evidencia adicional en JSON.
4. **Captura de al menos un request de cada tipo** (éxito 200/201, error 400, error 404) con el cuerpo de la respuesta visible.
5. **Captura de Swagger UI** de 2-3 microservicios mostrando los endpoints documentados.
6. **Salida de terminal** de `docker compose up --build` y `docker ps` mostrando los contenedores corriendo.

---

## 8. Notas técnicas

- Todas las requests usan `{{baseUrl}}` (Gateway), nunca puertos directos de microservicios, para evidenciar que el enrutamiento vía Eureka funciona end-to-end.
- Los `POST` y `PUT` incluyen el header `Content-Type: application/json` explícitamente.
- Los bodies de `POST`/`PUT` están armados según los campos reales de cada `*RequestDTO` (ver `src/main/java/.../dto/`), no inventados.
- Las variables `catalogoId`, `libroId`, `historialId`, `multaId`, `reservaId`, `detalleId` y `resenaId` se sobrescriben automáticamente cuando su `POST` de creación exitosa devuelve un `id` — el script de test usa `pm.environment.set(...)`.
- Las variables `rolClienteId`, `rolAdminId`, `rolAutorId`, `userAdminId`, `userAutorId`, `userClienteId`, `estadoDisponibleId` y `estadoPrestadoId` **no se sobrescriben**: permanecen ancladas a los datos semilla del `DataInitializer` de cada microservicio porque otras carpetas dependen de su identidad específica para reproducir reglas de negocio cruzadas (por ejemplo, el usuario id 6 con multas acumuladas).
