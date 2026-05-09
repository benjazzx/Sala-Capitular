package Biblioteca.example.Detalle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
public class DetalleApplication {
    public static void main(String[] args) {
        SpringApplication.run(DetalleApplication.class, args);
    }
}
