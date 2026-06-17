package Biblioteca.example.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayRoutes {

    @Bean
    public RouterFunction<ServerResponse> routes(
        @Value("${MS_ROL_URL:http://localhost:8081}") String rolUrl,
        @Value("${MS_USER_URL:http://localhost:8082}") String userUrl,
        @Value("${MS_CATALOGO_URL:http://localhost:8083}") String catalogoUrl,
        @Value("${MS_ESTADO_URL:http://localhost:8084}") String estadoUrl,
        @Value("${MS_LIBRO_URL:http://localhost:8085}") String libroUrl,
        @Value("${MS_ESTANTE_URL:http://localhost:8086}") String estanteUrl,
        @Value("${MS_HISTORIAL_URL:http://localhost:8087}") String historialUrl,
        @Value("${MS_MULTAS_URL:http://localhost:8088}") String multasUrl,
        @Value("${MS_RESERVA_URL:http://localhost:8089}") String reservaUrl,
        @Value("${MS_DETALLE_URL:http://localhost:8090}") String detalleUrl,
        @Value("${MS_RESENA_URL:http://localhost:8091}") String resenaUrl
    ) {
        return route("rol")
            .route(path("/api/roles/**"), http())
            .before(uri(rolUrl))
            .build()
        .and(route("user")
            .route(path("/api/users/**"), http())
            .before(uri(userUrl))
            .build())
        .and(route("catalogo")
            .route(path("/api/catalogos/**"), http())
            .before(uri(catalogoUrl))
            .build())
        .and(route("estado")
            .route(path("/api/estados/**"), http())
            .before(uri(estadoUrl))
            .build())
        .and(route("libro")
            .route(path("/api/libros/**"), http())
            .before(uri(libroUrl))
            .build())
        .and(route("estante")
            .route(path("/api/estantes/**"), http())
            .before(uri(estanteUrl))
            .build())
        .and(route("historial")
            .route(path("/api/historiales/**"), http())
            .before(uri(historialUrl))
            .build())
        .and(route("multas")
            .route(path("/api/multas/**"), http())
            .before(uri(multasUrl))
            .build())
        .and(route("reserva")
            .route(path("/api/reservas/**"), http())
            .before(uri(reservaUrl))
            .build())
        .and(route("detalle")
            .route(path("/api/detalles/**"), http())
            .before(uri(detalleUrl))
            .build())
        .and(route("resena")
            .route(path("/api/resenas/**"), http())
            .before(uri(resenaUrl))
            .build());
    }
}