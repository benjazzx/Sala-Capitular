package Biblioteca.example.Estante.controller;

import Biblioteca.example.Estante.dto.EstanteRequestDTO;
import Biblioteca.example.Estante.dto.EstanteResponseDTO;
import Biblioteca.example.Estante.service.EstanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estantes")
@RequiredArgsConstructor
@Tag(name = "Estantes", description = "Operaciones CRUD para la gestión de estantes y ubicación de libros")
public class EstanteController {

    private final EstanteService service;

    @GetMapping
    @Operation(
            summary = "Listar todos los estantes",
            description = "Retorna una lista con todos los estantes registrados en el sistema."
    )
    public List<EstanteResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar estante por ID",
            description = "Retorna un estante específico según su identificador."
    )
    public ResponseEntity<EstanteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Crear un nuevo estante",
            description = "Registra un nuevo estante asociado a un libro existente."
    )
    public ResponseEntity<EstanteResponseDTO> guardar(@Valid @RequestBody EstanteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un estante",
            description = "Actualiza los datos de un estante existente según su ID."
    )
    public ResponseEntity<EstanteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstanteRequestDTO dto
    ) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un estante",
            description = "Elimina un estante existente según su ID."
    )
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}