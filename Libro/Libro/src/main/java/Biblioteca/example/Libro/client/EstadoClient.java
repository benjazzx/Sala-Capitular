package Biblioteca.example.Libro.client;

import Biblioteca.example.Libro.dto.EstadoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-estado", url = "${ms.estado.url}")
public interface EstadoClient {
    @GetMapping("/api/estados/{id}")
    EstadoResponseDTO obtenerPorId(@PathVariable Long id);
}
