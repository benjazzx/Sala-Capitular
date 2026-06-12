package Biblioteca.example.ResenaLibro.controller;

import Biblioteca.example.ResenaLibro.dto.ResenaLibroRequestDTO;
import Biblioteca.example.ResenaLibro.dto.ResenaLibroResponseDTO;
import Biblioteca.example.ResenaLibro.service.ResenaLibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
@Tag(name = "Reseñas de Libros", description = "Operaciones para la gestión de reseñas de libros")
public class ResenaLibroController {

    private final ResenaLibroService service;

    @GetMapping
    @Operation(summary = "Listar todas las reseñas", description = "Retorna una lista con todas las reseñas registradas.")
    public List<ResenaLibroResponseDTO> obtenerTodos() { return service.obtenerTodos(); }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reseña por ID", description = "Retorna una reseña específica según su identificador.")
    public ResponseEntity<ResenaLibroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/libro/{libroId}")
    @Operation(summary = "Listar reseñas por libro", description = "Retorna todas las reseñas asociadas a un libro específico.")
    public List<ResenaLibroResponseDTO> obtenerPorLibro(@PathVariable Long libroId) {
        return service.obtenerPorLibro(libroId);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reseña", description = "Registra una nueva reseña para un libro.")
    public ResponseEntity<ResenaLibroResponseDTO> guardar(@Valid @RequestBody ResenaLibroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reseña", description = "Actualiza los datos de una reseña existente según su ID.")
    public ResponseEntity<ResenaLibroResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ResenaLibroRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reseña", description = "Elimina una reseña existente según su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
