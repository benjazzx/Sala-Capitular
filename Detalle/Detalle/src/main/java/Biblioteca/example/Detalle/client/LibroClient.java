package Biblioteca.example.Detalle.client;
import Biblioteca.example.Detalle.dto.LibroResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-libro-detalle", url = "${ms.libro.url}")
public interface LibroClient {
    @GetMapping("/api/libros/{id}")
    LibroResponseDTO obtenerPorId(@PathVariable Long id);
}
