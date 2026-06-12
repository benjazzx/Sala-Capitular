package Biblioteca.example.Libro.client;

import Biblioteca.example.Libro.dto.CatalogoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-catalogo", url = "${ms.catalogo.url}")
public interface CatalogoClient {
    @GetMapping("/api/catalogos/{id}")
    CatalogoResponseDTO obtenerPorId(@PathVariable Long id);
}
