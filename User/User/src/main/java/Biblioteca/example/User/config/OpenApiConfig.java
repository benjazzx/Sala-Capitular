package Biblioteca.example.User.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI userOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("User Microservice API")
                .description("Gestión de usuarios y autenticación en Sala Capitular")
                .version("1.0.0"));
    }
}
