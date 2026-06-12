package Biblioteca.example.Catalogo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI catalogoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio CatÃ¡logo - Sala Capitular")
                        .version("1.0.0")
                        .description("API REST para la gestiÃ³n de catÃ¡logos dentro del sistema Sala Capitular."));
    }
}