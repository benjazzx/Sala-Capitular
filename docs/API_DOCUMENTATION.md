# API Documentation — Sala Capitular

Cada microservicio expone su documentación interactiva Swagger UI en:

```
http://localhost:{puerto}/swagger-ui/index.html
```

Y el contrato OpenAPI JSON en:

```
http://localhost:{puerto}/v3/api-docs
```

## Resumen de endpoints

### Rol — 8081 — `/api/roles`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/roles` | Listar roles |
| GET | `/api/roles/{id}` | Obtener rol por ID |
| POST | `/api/roles` | Crear rol (CLIENTE, ADMIN o AUTOR) |
| PUT | `/api/roles/{id}` | Actualizar rol |
| DELETE | `/api/roles/{id}` | Eliminar rol |

### User — 8082 — `/api/users`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/users` | Listar usuarios |
| GET | `/api/users/{id}` | Obtener usuario por ID |
| POST | `/api/users/login` | Login (email + password) |
| POST | `/api/users` | Registrar usuario (no permite rol ADMIN) |
| PUT | `/api/users/{id}` | Actualizar usuario |
| DELETE | `/api/users/{id}` | Eliminar usuario |

### Catalogo — 8083 — `/api/catalogos`
CRUD estándar (GET, GET/{id}, POST, PUT/{id}, DELETE/{id}).

### Estado — 8084 — `/api/estados`
CRUD estándar.

### Libro — 8085 — `/api/libros`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/libros` | Listar libros |
| GET | `/api/libros/{id}` | Obtener libro por ID |
| GET | `/api/libros/autor/{autorId}` | Libros de un autor |
| POST | `/api/libros` | Crear libro (valida autor con rol AUTOR, catálogo, estado, ISBN único) |
| PUT | `/api/libros/{id}` | Actualizar libro |
| DELETE | `/api/libros/{id}` | Eliminar libro |

### Estante — 8086 — `/api/estantes`
CRUD estándar (valida que el libro exista).

### Historial — 8087 — `/api/historiales`
CRUD estándar (valida libro, usuario y estado).

### Multas — 8088 — `/api/multas`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/multas` | Listar multas |
| GET | `/api/multas/{id}` | Obtener multa por ID |
| GET | `/api/multas/usuario/{userId}` | Multas de un usuario |
| GET | `/api/multas/usuario/{userId}/puede-pedir` | ¿Puede reservar? (boolean) |
| GET | `/api/multas/usuario/{userId}/estado` | Estado consolidado de multas |
| GET | `/api/multas/no-entrega` | Multas tipo NO_ENTREGA |
| GET | `/api/multas/tipo/{tipo}` | Multas por tipo |
| POST | `/api/multas` | Crear multa (requiere admin con rol ADMIN) |
| PUT | `/api/multas/{id}` | Actualizar multa |
| DELETE | `/api/multas/{id}` | Eliminar multa |

### ReservaLibro — 8089 — `/api/reservas`
CRUD estándar. Al crear: valida usuario, libro, estado de multas (bloquea si total de puntos > 3) y que el libro no tenga ya una reserva ACTIVA.

### Detalle — 8090 — `/api/detalles`
CRUD estándar (valida historial y libro).

### ReseñaLibro — 8091 — `/api/resenas`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/resenas` | Listar reseñas |
| GET | `/api/resenas/{id}` | Obtener reseña por ID |
| GET | `/api/resenas/libro/{libroId}` | Reseñas de un libro |
| POST | `/api/resenas` | Crear reseña (calificación 1–5) |
| PUT | `/api/resenas/{id}` | Actualizar reseña |
| DELETE | `/api/resenas/{id}` | Eliminar reseña |

## Ejemplos de cuerpos JSON

**Login (POST /api/users/login):**
```json
{ "email": "cliente@mail.com", "password": "secreta123" }
```

**Crear reserva (POST /api/reservas):**
```json
{ "userId": 1, "libroId": 5, "fechaReserva": "2026-06-12", "estadoReserva": "ACTIVA" }
```

**Crear multa (POST /api/multas):**
```json
{
  "adminId": 1,
  "userId": 4,
  "historialId": 10,
  "descripcion": "Devolución con 5 días de retraso",
  "fecha": "2026-06-12",
  "cantidad": 2,
  "tipo": "RETRASO"
}
```

## Códigos de respuesta comunes

| Código | Significado |
|---|---|
| 200 OK | Operación exitosa (GET, PUT) |
| 201 Created | Recurso creado (POST) |
| 204 No Content | Recurso eliminado (DELETE) |
| 400 Bad Request | Validación fallida o regla de negocio violada |
| 404 Not Found | Recurso no encontrado (GET por ID) |
