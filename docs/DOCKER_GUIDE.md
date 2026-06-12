# Docker Guide — Sala Capitular

Esta guía describe cómo construir y ejecutar todo el sistema (11 microservicios + MySQL + API Gateway) con Docker Compose.

## Requisitos

- Docker Desktop instalado y en ejecución.
- Conexión a internet en el primer build (descarga de imágenes base y dependencias Maven).

## Estructura relevante

```
Sala-Capitular/
├── docker-compose.yml
├── .env.example
├── docker/
│   └── init/
│       └── 01-create-databases.sql   # crea las 11 bases al iniciar MySQL
├── api-gateway/Dockerfile
├── Rol/Rol/Dockerfile
├── User/User/Dockerfile
└── ...  (un Dockerfile por microservicio)
```

## Puesta en marcha

1. Copia el archivo de entorno y ajústalo si quieres cambiar la contraseña:

   ```bash
   cp .env.example .env
   ```

2. Construye y levanta todo:

   ```bash
   docker compose up --build
   ```

   La primera vez tardará varios minutos (compila los 12 módulos dentro de cada imagen).

3. El punto de entrada queda en el gateway:

   ```
   http://localhost:8080/api/...
   ```

   Por ejemplo: `http://localhost:8080/api/roles`, `http://localhost:8080/api/users/login`.

## Detalles de configuración

### Base de datos
- Se usa un único contenedor MySQL 8.4 que aloja las 11 bases.
- El script `docker/init/01-create-databases.sql` se ejecuta automáticamente la primera vez que arranca el contenedor (volumen `docker-entrypoint-initdb.d`).
- Puerto publicado en el host: **3307** (para no chocar con un MySQL/XAMPP local en 3306).

### Variables de entorno
Cada servicio recibe por Compose:
- `DB_URL`, `DB_USER`, `DB_PASSWORD` — conexión a MySQL (apunta al host `mysql` de la red interna).
- `MS_*_URL` — URLs de los servicios dependientes (apuntan a los nombres de servicio de Compose, no a `localhost`).

Los `application.properties` usan placeholders `${VAR:default}`, así que **sin** Docker (ejecución local con `.\mvnw.cmd spring-boot:run`) siguen funcionando con sus valores `localhost` por defecto.

### Orden de arranque
`depends_on` con `condition: service_healthy` para MySQL y `service_started` para las dependencias Feign reproduce el orden documentado en `DIAGNOSTICO_INICIAL.md`.

## Comandos útiles

```bash
# Levantar en segundo plano
docker compose up --build -d

# Ver logs de un servicio
docker compose logs -f libro

# Reconstruir un solo servicio
docker compose build user && docker compose up -d user

# Parar y eliminar contenedores (conserva datos)
docker compose down

# Parar y BORRAR también el volumen de datos MySQL
docker compose down -v
```

## Verificación

- Gateway sano: `http://localhost:8080/actuator/health`
- Rutas del gateway: `http://localhost:8080/actuator/gateway/routes`
- Swagger de un servicio (directo, sin pasar por gateway): `http://localhost:8085/swagger-ui/index.html`

## Solución de problemas

| Síntoma | Causa probable | Solución |
|---|---|---|
| Un servicio reinicia en bucle | MySQL aún no estaba listo | `depends_on healthy` ya lo cubre; si persiste, revisa logs de `mysql` |
| `Communications link failure` | `DB_URL` apunta a `localhost` en vez de `mysql` | Verifica que Compose inyecta `DB_URL` |
| Feign `Connection refused` | El servicio dependiente no está arriba | Revisa el orden / logs del servicio destino |
| Puerto 3307 ocupado | Otro MySQL en el host | Cambia el mapeo de puertos en `docker-compose.yml` |
