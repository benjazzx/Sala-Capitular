package Biblioteca.example.ResenaLibro;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
public class ResenaLibroApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResenaLibroApplication.class, args);
    }
}
