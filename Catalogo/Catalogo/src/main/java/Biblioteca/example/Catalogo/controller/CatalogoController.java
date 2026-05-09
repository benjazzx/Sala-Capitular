package Biblioteca.example.Catalogo.controller;

import Biblioteca.example.Catalogo.dto.CatalogoRequestDTO;
import Biblioteca.example.Catalogo.dto.CatalogoResponseDTO;
import Biblioteca.example.Catalogo.service.CatalogoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService service;

    @GetMapping
    public List<CatalogoResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CatalogoResponseDTO> guardar(@Valid @RequestBody CatalogoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CatalogoRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
