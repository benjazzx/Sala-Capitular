package Biblioteca.example.Rol.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI rolOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Rol Microservice API")
                .description("Gestión de roles de usuario en Sala Capitular")
                .version("1.0.0"));
    }
}
