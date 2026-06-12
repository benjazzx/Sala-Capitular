package Biblioteca.example.Estante.client;
import Biblioteca.example.Estante.dto.LibroResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-libro", url = "${ms.libro.url}")
public interface LibroClient {
    @GetMapping("/api/libros/{id}")
    LibroResponseDTO obtenerPorId(@PathVariable Long id);
}
