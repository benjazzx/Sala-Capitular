package Biblioteca.example.Detalle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Detalles - Sala Capitular")
                        .version("1.0.0")
                        .description("API REST para la gestión de detalles de préstamos dentro del sistema Sala Capitular."));
    }
}
