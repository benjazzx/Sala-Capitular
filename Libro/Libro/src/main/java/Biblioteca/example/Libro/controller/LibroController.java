package Biblioteca.example.Libro.controller;

import Biblioteca.example.Libro.dto.LibroRequestDTO;
import Biblioteca.example.Libro.dto.LibroResponseDTO;
import Biblioteca.example.Libro.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Libros", description = "Operaciones CRUD y consulta de libros")
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService service;

    @Operation(summary = "Listar recursos")
    @GetMapping
    public List<LibroResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @Operation(summary = "Listar libros por autor")
    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<LibroResponseDTO>> obtenerPorAutor(@PathVariable Long autorId) {
        return ResponseEntity.ok(service.obtenerPorAutor(autorId));
    }

    @Operation(summary = "Obtener recurso por id")
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear recurso")
    @PostMapping
    public ResponseEntity<LibroResponseDTO> guardar(@Valid @RequestBody LibroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @Operation(summary = "Actualizar recurso")
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody LibroRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar recurso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
