package Biblioteca.example.Estado.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI estadoOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Estado Microservice API")
                .description("Gestión de estados de préstamo en Sala Capitular")
                .version("1.0.0"));
    }
}
