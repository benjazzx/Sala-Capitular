package Biblioteca.example.ResenaLibro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI resenaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("ReseñaLibro Microservice API")
                .description("Gestión de reseñas de libros en Sala Capitular")
                .version("1.0.0"));
    }
}
