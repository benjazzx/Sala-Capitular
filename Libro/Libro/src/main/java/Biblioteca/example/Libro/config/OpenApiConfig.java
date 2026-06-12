package Biblioteca.example.Libro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI libroOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Libro Microservice API")
                .description("Gestión del catálogo de libros en Sala Capitular")
                .version("1.0.0"));
    }
}
