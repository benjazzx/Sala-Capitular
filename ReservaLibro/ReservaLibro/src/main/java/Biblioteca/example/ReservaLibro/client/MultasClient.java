package Biblioteca.example.ReservaLibro.client;
import Biblioteca.example.ReservaLibro.dto.EstadoMultasDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-multas-reserva", url = "${ms.multas.url}")
public interface MultasClient {
    @GetMapping("/api/multas/usuario/{userId}/puede-pedir")
    Boolean puedeReservar(@PathVariable Long userId);
    @GetMapping("/api/multas/usuario/{userId}/estado")
    EstadoMultasDTO obtenerEstado(@PathVariable Long userId);
}
