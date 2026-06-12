package Biblioteca.example.Multas.client;
import Biblioteca.example.Multas.dto.HistorialResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-historial-multas", url = "${ms.historial.url}")
public interface HistorialClient {
    @GetMapping("/api/historiales/{id}")
    HistorialResponseDTO obtenerPorId(@PathVariable Long id);
}
