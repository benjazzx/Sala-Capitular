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
@RequestMapping("/api/libros")
@RequiredArgsConstructor
@Tag(name = "Libros", description = "Operaciones para la gestión de libros")
public class LibroController {

    private final LibroService service;

    @GetMapping
    @Operation(summary = "Listar todos los libros", description = "Retorna una lista con todos los libros registrados.")
    public List<LibroResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/autor/{autorId}")
    @Operation(summary = "Listar libros por autor", description = "Retorna los libros de un autor específico.")
    public ResponseEntity<List<LibroResponseDTO>> obtenerPorAutor(@PathVariable Long autorId) {
        return ResponseEntity.ok(service.obtenerPorAutor(autorId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar libro por ID", description = "Retorna un libro específico según su identificador.")
    public ResponseEntity<LibroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo libro", description = "Registra un nuevo libro en el sistema.")
    public ResponseEntity<LibroResponseDTO> guardar(@Valid @RequestBody LibroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un libro", description = "Actualiza los datos de un libro existente según su ID.")
    public ResponseEntity<LibroResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody LibroRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro", description = "Elimina un libro existente según su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
