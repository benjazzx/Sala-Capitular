package Biblioteca.example.ReservaLibro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI reservaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("ReservaLibro Microservice API")
                .description("Gestión de reservas de libros en Sala Capitular")
                .version("1.0.0"));
    }
}
