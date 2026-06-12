package Biblioteca.example.Estante;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
public class EstanteApplication {
    public static void main(String[] args) {
        SpringApplication.run(EstanteApplication.class, args);
    }
}
