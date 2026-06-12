package Biblioteca.example.Multas;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
public class MultasApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultasApplication.class, args);
    }
}
