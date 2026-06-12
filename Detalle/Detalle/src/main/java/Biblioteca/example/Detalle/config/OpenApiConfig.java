package Biblioteca.example.Detalle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI detalleOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Detalle Microservice API")
                .description("Gestión de detalles de préstamo en Sala Capitular")
                .version("1.0.0"));
    }
}
