package Biblioteca.example.Historial.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI historialOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Historial Microservice API")
                .description("Gestión del historial de préstamos en Sala Capitular")
                .version("1.0.0"));
    }
}
