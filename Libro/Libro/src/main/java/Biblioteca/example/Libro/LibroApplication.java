package Biblioteca.example.Libro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LibroApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibroApplication.class, args);
    }
}
