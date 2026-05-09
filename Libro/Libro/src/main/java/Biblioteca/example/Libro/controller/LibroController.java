package Biblioteca.example.Libro.controller;

import Biblioteca.example.Libro.dto.LibroRequestDTO;
import Biblioteca.example.Libro.dto.LibroResponseDTO;
import Biblioteca.example.Libro.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService service;

    @GetMapping
    public List<LibroResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LibroResponseDTO> guardar(@Valid @RequestBody LibroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody LibroRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
