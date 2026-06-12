package Biblioteca.example.ReservaLibro;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
public class ReservaLibroApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReservaLibroApplication.class, args);
    }
}
