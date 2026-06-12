package Biblioteca.example.ReservaLibro.client;
import Biblioteca.example.ReservaLibro.dto.LibroResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-libro-reserva", url = "${ms.libro.url}")
public interface LibroClient {
    @GetMapping("/api/libros/{id}")
    LibroResponseDTO obtenerPorId(@PathVariable Long id);
}
