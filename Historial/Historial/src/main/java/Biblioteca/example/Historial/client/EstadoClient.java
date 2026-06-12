package Biblioteca.example.Historial.client;
import Biblioteca.example.Historial.dto.EstadoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-estado-historial", url = "${ms.estado.url}")
public interface EstadoClient {
    @GetMapping("/api/estados/{id}")
    EstadoResponseDTO obtenerPorId(@PathVariable Long id);
}
