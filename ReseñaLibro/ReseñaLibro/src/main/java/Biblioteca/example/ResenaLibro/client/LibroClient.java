package Biblioteca.example.ResenaLibro.client;
import Biblioteca.example.ResenaLibro.dto.LibroResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-libro-resena", url = "${ms.libro.url}")
public interface LibroClient {
    @GetMapping("/api/libros/{id}")
    LibroResponseDTO obtenerPorId(@PathVariable Long id);
}
