# Guía Eureka Server — Sala Capitular

**Puerto:** 8761
**URL consola:** http://localhost:8761

---

## ¿Qué es Eureka en este proyecto?

El Eureka Server actúa como **servidor de descubrimiento de servicios**. Todos los microservicios se registran en él al arrancar y el API Gateway lo consulta para resolver las URIs `lb://nombre-servicio` → instancia real.

---

## Cómo ejecutar Eureka

### Opción A — Ejecución manual (Windows)
```powershell
cd eurekaserver
.\mvnw.cmd spring-boot:run
```

### Opción B — Ejecución manual (Linux/macOS)
```bash
cd eurekaserver
./mvnw spring-boot:run
```

### Opción C — Docker Compose (junto con todo el sistema)
```bash
docker compose up eureka-server
```

---

## Consola de administración

Una vez levantado:
- **URL:** http://localhost:8761
- Se puede ver qué instancias están registradas bajo "Instances currently registered with Eureka"

---

## Microservicios que se registran en Eureka

| Microservicio | Nombre en Eureka         | Puerto |
|--------------|--------------------------|--------|
| Rol          | ROL-SERVICE              | 8081   |
| User         | USER-SERVICE             | 8082   |
| Catalogo     | CATALOGO-SERVICE         | 8083   |
| Estado       | ESTADO-SERVICE           | 8084   |
| Libro        | LIBRO-SERVICE            | 8085   |
| Estante      | ESTANTE-SERVICE          | 8086   |
| Historial    | HISTORIAL-SERVICE        | 8087   |
| Multas       | MULTAS-SERVICE           | 8088   |
| ReservaLibro | RESERVA-LIBRO-SERVICE    | 8089   |
| Detalle      | DETALLE-SERVICE          | 8090   |
| ReseñaLibro  | RESENA-LIBRO-SERVICE     | 8091   |
| API Gateway  | API-GATEWAY              | 8080   |

> Eureka normaliza los nombres a MAYÚSCULAS internamente. Las URIs `lb://` son case-insensitive.

---

## Orden de arranque recomendado

1. **MySQL** (base de datos)
2. **eurekaserver** (servidor de descubrimiento)
3. **rol**, **catalogo**, **estado** (servicios base sin dependencias)
4. **user** (depende de rol)
5. **libro** (depende de catalogo, estado, user)
6. **estante**, **historial**, **multas** (dependen de libro/user)
7. **detalle**, **resena**, **reserva** (dependen de historial/libro/multas)
8. **api-gateway** (último, ya que consulta Eureka para enrutar)

---

## Configuración del Servidor Eureka

**eurekaserver/src/main/resources/application.properties:**
```properties
spring.application.name=eurekaserver
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.service-url.defaultZone=http://eureka-server:8761/eureka/
eureka.instance.hostname=eureka-server
```

> `register-with-eureka=false` y `fetch-registry=false` evitan que el servidor se registre a sí mismo.

---

## Configuración de Clientes Eureka (cada microservicio)

```properties
spring.application.name=rol-service
eureka.client.service-url.defaultZone=${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:http://localhost:8761/eureka/}
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=${EUREKA_INSTANCE_PREFER_IP_ADDRESS:false}
```

---

## Problemas comunes

| Problema | Causa | Solución |
|---------|-------|----------|
| Servicio no aparece en Eureka | Eureka no levantó antes que el microservicio | Esperar ~30s o configurar retry |
| Gateway devuelve 503 | Servicio no registrado aún en Eureka | Esperar heartbeat de Eureka (30s por defecto) |
| `EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP` | Solo una instancia y se desconectó | Normal en desarrollo con una sola instancia |
| Servicio aparece como DOWN en consola | Falla en el healthcheck de Eureka | Revisar logs del microservicio |
