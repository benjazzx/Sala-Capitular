package Biblioteca.example.User.client;

import Biblioteca.example.User.dto.RolResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-rol", url = "${ms.rol.url}")
public interface RolClient {
    @GetMapping("/api/roles/{id}")
    RolResponseDTO obtenerPorId(@PathVariable Long id);
}
